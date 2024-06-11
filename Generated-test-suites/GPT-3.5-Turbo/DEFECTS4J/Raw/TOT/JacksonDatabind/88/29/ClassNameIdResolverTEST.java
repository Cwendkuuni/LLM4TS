package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver classNameIdResolver;
    private JavaType baseType;
    private TypeFactory typeFactory;
    private DatabindContext context;
    private String id;

    @Before
    public void setUp() {
        baseType = Mockito.mock(JavaType.class);
        typeFactory = Mockito.mock(TypeFactory.class);
        context = Mockito.mock(DatabindContext.class);
        id = "com.example.MyClass";
        classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, classNameIdResolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        // No specific test case needed as the method does nothing for class name-based resolvers
    }

    @Test
    public void testIdFromValue() {
        Object value = new Object();
        String expected = "java.lang.Object";

        String result = classNameIdResolver.idFromValue(value);

        assertEquals(expected, result);
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> type = value.getClass();
        String expected = "java.lang.Object";

        String result = classNameIdResolver.idFromValueAndType(value, type);

        assertEquals(expected, result);
    }

    @Test
    public void testTypeFromId() throws IOException {
        JavaType expected = Mockito.mock(JavaType.class);
        Mockito.when(typeFactory.constructFromCanonical(id)).thenReturn(expected);

        JavaType result = classNameIdResolver.typeFromId(context, id);

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidId() throws IOException {
        Mockito.when(typeFactory.findClass(id)).thenThrow(ClassNotFoundException.class);

        classNameIdResolver.typeFromId(context, id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidIdType() throws IOException {
        Mockito.when(typeFactory.findClass(id)).thenThrow(NullPointerException.class);

        classNameIdResolver.typeFromId(context, id);
    }
    
    @Test
    public void testTypeFromIdWithUnknownId() throws IOException {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Mockito.when(context instanceof DeserializationContext).thenReturn(true);
        Mockito.when(context.handleUnknownTypeId(baseType, id, classNameIdResolver, "no such class found")).thenReturn(Mockito.mock(JavaType.class));
        
        JavaType result = classNameIdResolver.typeFromId(context, id);

        assertNotNull(result);
    }

    @Test
    public void testTypeFromIdWithNoClass() throws IOException {
        Mockito.when(typeFactory.findClass(id)).thenReturn(null);

        JavaType result = classNameIdResolver.typeFromId(context, id);

        assertNull(result);
    }

    @Test
    public void testTypeFromIdWithInnerClass() throws IOException {
        Mockito.when(typeFactory.findClass(id)).thenReturn(Mockito.mock(Class.class));

        JavaType result = classNameIdResolver.typeFromId(context, id);

        assertNotNull(result);
    }

    @Test
    public void testTypeFromIdWithNonStaticInnerClass() throws IOException {
        Class<?> staticType = Mockito.mock(Class.class);
        Mockito.when(baseType.getRawClass()).thenReturn(staticType);
        Mockito.when(ClassUtil.getOuterClass(staticType)).thenReturn(null);
        Mockito.when(typeFactory.findClass(id)).thenReturn(Mockito.mock(Class.class));

        JavaType result = classNameIdResolver.typeFromId(context, id);

        assertNotNull(result);
    }

    @Test
    public void testTypeFromIdWithStaticInnerClass() throws IOException {
        Class<?> staticType = Mockito.mock(Class.class);
        Mockito.when(baseType.getRawClass()).thenReturn(null);
        Mockito.when(ClassUtil.getOuterClass(staticType)).thenReturn(staticType);
        Mockito.when(typeFactory.findClass(id)).thenReturn(staticType);

        JavaType result = classNameIdResolver.typeFromId(context, id);

        assertNotNull(result);
    }

    @Test
    public void testIdFromValueWhenParameterValueIsNull() {
        Object value = null;
        String expected = "null";

        String result = classNameIdResolver.idFromValue(value);

        assertEquals(expected, result);
    }

    @Test
    public void testIdFromValueAndTypeWhenParameterValueIsNull() {
        Object value = null;
        Class<?> type = Object.class;
        String expected = "null";

        String result = classNameIdResolver.idFromValueAndType(value, type);

        assertEquals(expected, result);
    }

    @After
    public void tearDown() {
        baseType = null;
        typeFactory = null;
        context = null;
        id = null;
        classNameIdResolver = null;
    }

}