import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;

import org.junit.Before;
import org.junit.Test;

public class StdValueInstantiatorTest {

    private StdValueInstantiator stdValueInstantiator;
    private DeserializationContext deserializationContext;
    private JavaType javaType;
    private AnnotatedWithParams annotatedWithParams;
    private SettableBeanProperty[] settableBeanProperties;

    @Before
    public void setUp() {
        deserializationContext = mock(DeserializationContext.class);
        javaType = mock(JavaType.class);
        annotatedWithParams = mock(AnnotatedWithParams.class);
        settableBeanProperties = new SettableBeanProperty[0];

        stdValueInstantiator = new StdValueInstantiator(deserializationContext, javaType);
    }

    @Test
    public void testGetValueTypeDesc() {
        assertEquals("UNKNOWN TYPE", stdValueInstantiator.getValueTypeDesc());
    }

    @Test
    public void testGetValueClass() {
        assertEquals(Object.class, stdValueInstantiator.getValueClass());
    }

    @Test
    public void testCanCreateFromString() {
        when(stdValueInstantiator._fromStringCreator).thenReturn(annotatedWithParams);
        assertEquals(true, stdValueInstantiator.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt() {
        when(stdValueInstantiator._fromIntCreator).thenReturn(annotatedWithParams);
        assertEquals(true, stdValueInstantiator.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong() {
        when(stdValueInstantiator._fromLongCreator).thenReturn(annotatedWithParams);
        assertEquals(true, stdValueInstantiator.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() {
        when(stdValueInstantiator._fromDoubleCreator).thenReturn(annotatedWithParams);
        assertEquals(true, stdValueInstantiator.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() {
        when(stdValueInstantiator._fromBooleanCreator).thenReturn(annotatedWithParams);
        assertEquals(true, stdValueInstantiator.canCreateFromBoolean());
    }

    @Test
    public void testCanCreateUsingDefault() {
        when(stdValueInstantiator._defaultCreator).thenReturn(annotatedWithParams);
        assertEquals(true, stdValueInstantiator.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateUsingDelegate() {
        when(stdValueInstantiator._delegateCreator).thenReturn(annotatedWithParams);
        assertEquals(true, stdValueInstantiator.canCreateUsingDelegate());
    }

    @Test
    public void testCanCreateUsingArrayDelegate() {
        when(stdValueInstantiator._arrayDelegateCreator).thenReturn(annotatedWithParams);
        assertEquals(true, stdValueInstantiator.canCreateUsingArrayDelegate());
    }

    @Test
    public void testCanCreateFromObjectWith() {
        when(stdValueInstantiator._withArgsCreator).thenReturn(annotatedWithParams);
        assertEquals(true, stdValueInstantiator.canCreateFromObjectWith());
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateUsingDefaultNullContext() {
        stdValueInstantiator = new StdValueInstantiator(null, javaType);
        stdValueInstantiator.createUsingDefault(deserializationContext);
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        when(stdValueInstantiator._defaultCreator.call()).thenReturn(new Object());
        assertEquals(new Object(), stdValueInstantiator.createUsingDefault(deserializationContext));
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateFromObjectWithNullContext() {
        stdValueInstantiator.createFromObjectWith(deserializationContext, new Object[0]);
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        when(stdValueInstantiator._withArgsCreator.call(new Object[0])).thenReturn(new Object());
        assertEquals(new Object(), stdValueInstantiator.createFromObjectWith(deserializationContext, new Object[0]));
    }

    @Test
    public void testCreateUsingDelegateNullContext() {
        stdValueInstantiator.createUsingDelegate(deserializationContext, new Object());
    }

    @Test
    public void testCreateUsingDelegateObject() throws IOException {
        when(stdValueInstantiator._delegateCreator.call(settableBeanProperties)).thenReturn(new Object());
        assertEquals(new Object(), stdValueInstantiator.createUsingDelegate(deserializationContext, new Object()));
    }

    @Test
    public void testCreateUsingArrayDelegateNullContext() {
        stdValueInstantiator.createUsingArrayDelegate(deserializationContext, new Object());
    }

    @Test
    public void testCreateUsingArrayDelegateObject() throws IOException {
        when(stdValueInstantiator._arrayDelegateCreator.call(settableBeanProperties)).thenReturn(new Object());
        assertEquals(new Object(), stdValueInstantiator.createUsingArrayDelegate(deserializationContext, new Object()));
    }

    @Test
    public void testCreateFromStringNullContext() {
        stdValueInstantiator.createFromString(deserializationContext, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateFromStringNullValue() throws IOException {
        when(stdValueInstantiator._fromStringCreator.call1(null)).thenReturn(null);
        stdValueInstantiator.createFromString(deserializationContext, null);
    }

    @Test
    public void testCreateFromString() throws IOException {
        when(stdValueInstantiator._fromStringCreator.call1("value")).thenReturn(new Object());
        assertEquals(new Object(), stdValueInstantiator.createFromString(deserializationContext, "value"));
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateFromIntNullContext() {
        stdValueInstantiator.createFromInt(deserializationContext, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateFromIntNegativeValue() throws IOException {
        stdValueInstantiator.createFromInt(deserializationContext, -1);
    }

    @Test
    public void testCreateFromIntPositiveValue() throws IOException {
        when(stdValueInstantiator._fromIntCreator.call1(Integer.valueOf(1))).thenReturn(new Object());
        assertEquals(new Object(), stdValueInstantiator.createFromInt(deserializationContext, 1));
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateFromLongNullContext() {
        stdValueInstantiator.createFromLong(deserializationContext, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateFromLongNegativeValue() throws IOException {
        stdValueInstantiator.createFromLong(deserializationContext, -1L);
    }

    @Test
    public void testCreateFromLongPositiveValue() throws IOException {
        when(stdValueInstantiator._fromLongCreator.call1(Long.valueOf(1))).thenReturn(new Object());
        assertEquals(new Object(), stdValueInstantiator.createFromLong(deserializationContext, 1));
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateFromDoubleNullContext() {
        stdValueInstantiator.createFromDouble(deserializationContext, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateFromDoubleNegativeValue() throws IOException {
        stdValueInstantiator.createFromDouble(deserializationContext, -1.0);
    }

    @Test
    public void testCreateFromDoublePositiveValue() throws IOException {
        when(stdValueInstantiator._fromDoubleCreator.call1(Double.valueOf(1.0))).thenReturn(new Object());
        assertEquals(new Object(), stdValueInstantiator.createFromDouble(deserializationContext, 1.0));
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateFromBooleanNullContext() {
        stdValueInstantiator.createFromBoolean(deserializationContext, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateFromBooleanNegativeValue() throws IOException {
        stdValueInstantiator.createFromBoolean(deserializationContext, false);
    }

    @Test
    public void testCreateFromBooleanPositiveValue() throws IOException {
        when(stdValueInstantiator._fromBooleanCreator.call1(Boolean.valueOf(true))).thenReturn(new Object());
        assertEquals(new Object(), stdValueInstantiator.createFromBoolean(deserializationContext, true));
    }
}