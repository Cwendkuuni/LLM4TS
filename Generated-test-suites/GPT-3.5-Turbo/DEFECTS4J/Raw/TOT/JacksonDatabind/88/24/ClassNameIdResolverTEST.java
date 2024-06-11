import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonTypeInfo;

import org.junit.Test;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        JavaType baseTypeMock = mock(JavaType.class);
        TypeFactory typeFactoryMock = mock(TypeFactory.class);
        
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseTypeMock, typeFactoryMock);
        
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }
    
    @Test
    public void testRegisterSubtype() {
        // As registerSubtype method is not used, it does not require a test case
    }
    
    @Test
    public void testIdFromValue() {
        DatabindContext contextMock = mock(DatabindContext.class);
        TypeFactory typeFactoryMock = mock(TypeFactory.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.class, typeFactoryMock);
        Object valueMock = mock(Object.class);
        Class<?> valueClassMock = mock(Class.class);
        String expected = "SomeClass";
        
        when(valueMock.getClass()).thenReturn(valueClassMock);
        when(typeFactoryMock.constructSpecializedType(JavaType.class, valueClassMock)).thenReturn(JavaType.class);
        
        String result = resolver.idFromValue(valueMock);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIdFromValueAndType() {
        DatabindContext contextMock = mock(DatabindContext.class);
        TypeFactory typeFactoryMock = mock(TypeFactory.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.class, typeFactoryMock);
        Object valueMock = mock(Object.class);
        Class<?> typeMock = mock(Class.class);
        String expected = "SomeClass";
        
        when(typeFactoryMock.constructSpecializedType(JavaType.class, typeMock)).thenReturn(JavaType.class);
        
        String result = resolver.idFromValueAndType(valueMock, typeMock);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testTypeFromId() {
        DatabindContext contextMock = mock(DatabindContext.class);
        TypeFactory typeFactoryMock = mock(TypeFactory.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.class, typeFactoryMock);
        String id = "SomeClass";
        String unknownId = "UnknownClass";
        JavaType expected = JavaType.class;
        
        when(typeFactoryMock.findClass(id)).thenReturn(JavaType.class);
        when(typeFactoryMock.findClass(unknownId)).thenThrow(new ClassNotFoundException());
        
        JavaType result = resolver.typeFromId(contextMock, id);
        JavaType resultForUnknownId = resolver.typeFromId(contextMock, unknownId);
        
        assertEquals(expected, result);
        assertEquals(null, resultForUnknownId);
    }
    
    @Test
    public void test_typeFromId_withInvalidId() throws IOException {
        DatabindContext contextMock = mock(DatabindContext.class);
        TypeFactory typeFactoryMock = mock(TypeFactory.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.class, typeFactoryMock);
        String invalidId = "InvalidClass";
        
        when(typeFactoryMock.findClass(invalidId)).thenThrow(new IllegalArgumentException());
        
        try {
            resolver.typeFromId(contextMock, invalidId);
        } catch (IOException e) {
            assertEquals("Invalid type id 'InvalidClass' (for id type 'Id.class'): null", e.getMessage());
            throw e;
        }
    }
    
    @Test
    public void test_typeFromId_withDeserializationContextWhenClassNotFound() throws IOException {
        DeserializationContext contextMock = mock(DeserializationContext.class);
        TypeFactory typeFactoryMock = mock(TypeFactory.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.class, typeFactoryMock);
        String unknownId = "UnknownClass";
        JavaType baseTypeMock = mock(JavaType.class);
        String expectedMessage = "no such class found";
        
        when(contextMock.handleUnknownTypeId(baseTypeMock, unknownId, resolver, expectedMessage)).thenReturn(baseTypeMock);
        
        JavaType result = resolver.typeFromId(contextMock, unknownId);
        
        assertEquals(baseTypeMock, result);
    }
    
    @Test
    public void test_typeFromId_withDeserializationContextWhenExceptionThrown() throws IOException {
        DeserializationContext contextMock = mock(DeserializationContext.class);
        TypeFactory typeFactoryMock = mock(TypeFactory.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.class, typeFactoryMock);
        String id = "SomeClass";
        JavaType expected = JavaType.class;
        
        when(typeFactoryMock.findClass(id)).thenThrow(new Exception());
        
        try {
            resolver.typeFromId(contextMock, id);
        } catch (IOException e) {
            assertEquals("Invalid type id 'SomeClass' (for id type 'Id.class'): null", e.getMessage());
            throw e;
        }
    }
    
    @Test
    public void test_idFromValue_withEnumSet() {
        TypeFactory typeFactoryMock = mock(TypeFactory.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.class, typeFactoryMock);
        EnumSet<?> value = mock(EnumSet.class);
        Class<?> enumClass = mock(Class.class);
        String expected = "EnumSet<SomeEnum>";
        
        when(ClassUtil.findEnumType(value)).thenReturn(enumClass);
        when(typeFactoryMock.constructCollectionType(EnumSet.class, enumClass)).thenReturn(JavaType.class);
        
        String result = resolver._idFrom(value, EnumSet.class, typeFactoryMock);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void test_idFromValue_withEnum() {
        TypeFactory typeFactoryMock = mock(TypeFactory.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.class, typeFactoryMock);
        Enum<?> value = mock(Enum.class);
        String expected = "SomeEnum";
        
        when(value.getClass()).thenReturn(Enum.class);
        
        String result = resolver._idFrom(value, Enum.class, typeFactoryMock);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void test_idFromValue_withStringStartingWithJavaUtilAndEnumSet() {
        TypeFactory typeFactoryMock = mock(TypeFactory.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.class, typeFactoryMock);
        Class<?> clazzMock = mock(Class.class);
        String value = "Value";
        String end = ".Arrays$";
        String expected = "java.util.ArrayList";
        
        when(clazzMock.getName()).thenReturn("java.util.Arrays$List");
        
        String result = resolver._idFrom(value, clazzMock, typeFactoryMock);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void test_idFromValue_withInnerClassInStaticClass() {
        TypeFactory typeFactoryMock = mock(TypeFactory.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.class, typeFactoryMock);
        Class<?> clazzMock = mock(Class.class);
        Class<?> outerClassMock = mock(Class.class);
        String value = "Value";
        String outerClassName = "OuterClass";
        String expected = "InnerClass";
        
        when(ClassUtil.getOuterClass(clazzMock)).thenReturn(outerClassMock);
        when(ClassUtil.getOuterClass(outerClassMock)).thenReturn(null);
        when(clazzMock.getName()).thenReturn(expected);
        
        String result = resolver._idFrom(value, clazzMock, typeFactoryMock);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void test_getDescForKnownTypeIds() {
        TypeFactory typeFactoryMock = mock(TypeFactory.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.class, typeFactoryMock);
        String expected = "class name used as type id";
        
        String result = resolver.getDescForKnownTypeIds();
        
        assertEquals(expected, result);
    }
    
}