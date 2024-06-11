import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    @Mock
    private JavaType mockBaseType;

    @Mock
    private TypeFactory mockTypeFactory;

    private ClassNameIdResolver classNameIdResolver;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        classNameIdResolver = new ClassNameIdResolver(mockBaseType, mockTypeFactory);
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, classNameIdResolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        Class<?> mockType = mock(Class.class);
        String mockName = "testName";

        classNameIdResolver.registerSubtype(mockType, mockName);

        // Verify that the method is not used with class name - based resolvers
        verifyZeroInteractions(mockType, mockName);
    }

    @Test
    public void testIdFromValue() {
        Object mockValue = mock(Object.class);
        Class<?> mockClass = mockValue.getClass();

        String actual = classNameIdResolver.idFromValue(mockValue);

        assertEquals(classNameIdResolver._idFrom(mockValue, mockClass, mockTypeFactory), actual);
    }

    @Test
    public void testIdFromValueAndType() {
        Object mockValue = mock(Object.class);
        Class<?> mockClass = mock(Class.class);

        String actual = classNameIdResolver.idFromValueAndType(mockValue, mockClass);

        assertEquals(classNameIdResolver._idFrom(mockValue, mockClass, mockTypeFactory), actual);
    }

    @Test
    public void testTypeFromIdWithoutContext() throws IOException {
        String mockId = "testId";
        JavaType expected = classNameIdResolver._typeFromId(mockId, classNameIdResolver);

        DeserializationContext mockContext = mock(DeserializationContext.class);
        when(mockContext.getTypeFactory()).thenReturn(mockTypeFactory);

        JavaType actual = classNameIdResolver.typeFromId(mockContext, mockId);

        assertEquals(expected, actual);
    }

    @Test
    public void testTypeFromIdWithUnknownId() throws IOException {
        String mockId = "testId";
        String mockErrorMessage = "no such class found";
        JavaType mockBaseType = mock(JavaType.class);

        DeserializationContext mockContext = mock(DeserializationContext.class);
        when(mockContext.getTypeFactory()).thenReturn(mockTypeFactory);
        when(mockContext.handleUnknownTypeId(mockBaseType, mockId, classNameIdResolver, mockErrorMessage))
                .thenReturn(null);

        JavaType actual = classNameIdResolver.typeFromId(mockContext, mockId);

        assertNull(actual);
    }

    @Test
    public void testTypeFromIdWithException() throws IOException {
        String mockId = "testId";
        String mockErrorMessage = "Invalid type id " + mockId + " (for id type 'Id.class')";
        Exception mockException = mock(Exception.class);
        when(mockException.getMessage()).thenReturn(mockErrorMessage);

        DeserializationContext mockContext = mock(DeserializationContext.class);
        when(mockContext.getTypeFactory()).thenReturn(mockTypeFactory);
        when(mockTypeFactory.findClass(mockId)).thenThrow(mockException);

        try {
            classNameIdResolver.typeFromId(mockContext, mockId);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals(mockErrorMessage, e.getMessage());
        }
    }

    @Test
    public void testTypeFromId() throws IOException {
        String mockId = "testId";
        JavaType expected = classNameIdResolver._typeFromId(mockId, classNameIdResolver);

        DeserializationContext mockContext = mock(DeserializationContext.class);
        when(mockContext.getTypeFactory()).thenReturn(mockTypeFactory);
        when(mockTypeFactory.findClass(mockId)).thenReturn(Object.class);

        JavaType actual = classNameIdResolver.typeFromId(mockContext, mockId);

        assertEquals(expected, actual);
    }

    @Test
    public void testTypeFromIdWithInnerClass() throws IOException {
        String mockId = "testId";
        String mockClassName = "testClassName";
        Class<?> mockOuterClass = mock(Class.class);
        Class<?> mockStaticType = mock(Class.class);
        JavaType mockBaseType = mock(JavaType.class);
        JavaType expected = mock(JavaType.class);

        when(mockBaseType.getRawClass()).thenReturn(mockStaticType);
        when(ClassUtil.getOuterClass(mockStaticType)).thenReturn(mockOuterClass);
        when(ClassUtil.getOuterClass(mockOuterClass)).thenReturn(null);
        when(mockTypeFactory.constructSpecializedType(mockBaseType, mockStaticType)).thenReturn(expected);
        when(mockTypeFactory.findClass(mockId)).thenReturn(mockClassName.getClass());

        DeserializationContext mockContext = mock(DeserializationContext.class);
        when(mockContext.getTypeFactory()).thenReturn(mockTypeFactory);

        JavaType actual = classNameIdResolver.typeFromId(mockContext, mockId);

        assertEquals(expected, actual);
    }

    @Test
    public void test_idFrom() {
        Object mockValue = mock(Object.class);
        Class<?> mockClass = mockValue.getClass();

        String actual = classNameIdResolver._idFrom(mockValue, mockClass, mockTypeFactory);

        // Need to ensure that "enum subtypes" work too
        if (Enum.class.isAssignableFrom(mockClass) && !mockClass.isEnum()) {
            mockClass = mockClass.getSuperclass();
        }
        String expected = mockClass.getName();
        if (expected.startsWith("java.util")) {
            if (mockValue instanceof EnumSet<?>) {
                Class<?> enumClass = ClassUtil.findEnumType((EnumSet<?>) mockValue);
                expected = mockTypeFactory.constructCollectionType(EnumSet.class, enumClass).toCanonical();
            } else if (mockValue instanceof EnumMap<?, ?>) {
                Class<?> enumClass = ClassUtil.findEnumType((EnumMap<?, ?>) mockValue);
                Class<?> valueClass = Object.class;
                expected = mockTypeFactory.constructMapType(EnumMap.class, enumClass, valueClass).toCanonical();
            } else {
                String end = expected.substring(9);
                if ((end.startsWith(".Arrays$") || end.startsWith(".Collections$"))
                        && expected.indexOf("List") >= 0) {
                    expected = "java.util.ArrayList";
                }
            }
        } else if (expected.indexOf('$') >= 0) {
            Class<?> outer = ClassUtil.getOuterClass(mockClass);
            if (outer != null && ClassUtil.getOuterClass(mockStaticType) == null) {
                mockClass = mockStaticType;
                expected = mockClass.getName();
            }
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        assertEquals("class name used as type id", classNameIdResolver.getDescForKnownTypeIds());
    }
}