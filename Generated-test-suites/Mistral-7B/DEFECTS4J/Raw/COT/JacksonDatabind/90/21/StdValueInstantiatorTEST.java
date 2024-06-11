package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test cases for the StdValueInstantiator class.
 */
public class StdValueInstantiatorTest {

    private DeserializationContext context;
    private StdValueInstantiator stdValueInstantiator;

    @Before
    public void setup() {
        context = mock(DeserializationContext.class);
        stdValueInstantiator = new StdValueInstantiator(null, null);
    }

    /*
     * Test for default constructor
     */
    @Test
    public void testDefaultConstructor() {
        assertEquals("UNKNOWN TYPE", stdValueInstantiator.getValueTypeDesc());
        assertEquals(Object.class, stdValueInstantiator.getValueClass());
        assertNull(stdValueInstantiator.getDefaultCreator());
    }

    /*
     * Test for constructor with JavaType
     */
    @Test
    public void testConstructorWithJavaType() {
        JavaType javaType = mock(JavaType.class);
        stdValueInstantiator = new StdValueInstantiator(null, javaType);
        assertEquals("UNKNOWN TYPE", javaType.toString(), stdValueInstantiator.getValueTypeDesc());
        assertEquals(javaType.getRawClass(), stdValueInstantiator.getValueClass());
    }

    /*
     * Test for copy constructor
     */
    @Test
    public void testCopyConstructor() {
        StdValueInstantiator source = new StdValueInstantiator(null, null);
        StdValueInstantiator target = new StdValueInstantiator(source);
        assertEquals(source._valueTypeDesc, target._valueTypeDesc);
        assertEquals(source._valueClass, target._valueClass);
        assertEquals(source._defaultCreator, target._defaultCreator);
    }

    /*
     * Test for createUsingDefault() method
     */
    @Test
    public void testCreateUsingDefault() throws IOException {
        Object result = stdValueInstantiator.createUsingDefault(context);
        assertNull(result);
    }

    /*
     * Test for createFromObjectWith() method
     */
    @Test
    public void testCreateFromObjectWith() throws IOException {
        Object[] args = new Object[1];
        args[0] = "test";
        when(context.findInjectableValue(Mockito.any(SettableBeanProperty.class), Mockito.any(), Mockito.any())).thenReturn("testArg");
        when(context.handleInstantiationProblem(Mockito.any(Class.class), Mockito.anyArray(), Mockito.any(Throwable.class))).thenAnswer(invocation -> invocation.getArguments()[1]);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] arguments = new SettableBeanProperty[1];
        arguments[0] = mock(SettableBeanProperty.class);
        stdValueInstantiator.configureFromObjectSettings(creator, null, null, null, null, arguments);
        when(creator.call(args)).thenReturn(new Object() {});
        Object result = stdValueInstantiator.createFromObjectWith(context, args);
        assertEquals(new Object(){}, result);
    }

    /*
     * Test for createUsingDelegate() method with no delegate creator
     */
    @Test(expected = IllegalStateException.class)
    public void testCreateUsingDelegateNoDelegateCreator() throws IOException {
        stdValueInstantiator.createUsingDelegate(context, Mockito.mock(Object.class));
    }

    /*
     * Test for createUsingDelegate() method with delegate creator
     */
    @Test
    public void testCreateUsingDelegateWithDelegateCreator() throws IOException {
        Object delegate = new Object() {};
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] arguments = new SettableBeanProperty[1];
        arguments[0] = mock(SettableBeanProperty.class);
        stdValueInstantiator.configureFromObjectSettings(null, creator, null, null, null, arguments);
        when(context.findInjectableValue(Mockito.any(SettableBeanProperty.class), Mockito.any(), Mockito.any())).thenReturn(delegate);
        when(creator.call(arguments)).thenReturn(new Object() {});
        Object result = stdValueInstantiator.createUsingDelegate(context, delegate);
        assertEquals(new Object(){}, result);
    }

    /*
     * Test for createUsingArrayDelegate() method with no array delegate creator
     */
    @Test
    public void testCreateUsingArrayDelegateNoArrayDelegateCreator() throws IOException {
        stdValueInstantiator.createUsingArrayDelegate(context, Mockito.mock(Object.class));
    }

    /*
     * Test for createUsingArrayDelegate() method with array delegate creator
     */
    @Test
    public void testCreateUsingArrayDelegateWithArrayDelegateCreator() throws IOException {
        Object delegate = new Object() {};
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] arguments = new SettableBeanProperty[1];
        arguments[0] = mock(SettableBeanProperty.class);
        stdValueInstantiator.configureFromArraySettings(creator, null, null);
        when(context.findInjectableValue(Mockito.any(SettableBeanProperty.class), Mockito.any(), Mockito.any())).thenReturn(delegate);
        when(creator.call(arguments)).thenReturn(new Object() {});
        Object result = stdValueInstantiator.createUsingArrayDelegate(context, delegate);
        assertEquals(new Object(){}, result);
    }

    /*
     * Test for createFromString() method with no creator
     */
    @Test
    public void testCreateFromStringNoCreator() throws IOException {
        String value = "test";
        when(context.handleInstantiationProblem(Mockito.any(Class.class), Mockito.anyString(), Mockito.any(Throwable.class))).thenAnswer(invocation -> invocation.getArguments()[1]);
        Object result = stdValueInstantiator.createFromString(context, value);
        assertNull(result);
    }

    /*
     * Test for createFromInt() method with no creator
     */
    @Test
    public void testCreateFromIntNoCreator() throws IOException {
        int value = 1;
        Object result = stdValueInstantiator.createFromInt(context, value);
        assertNull(result);
    }

    /*
     * Test for createFromInt() method with int creator
     */
    @Test
    public void testCreateFromIntWithIntCreator() throws IOException {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        when(context.handleInstantiationProblem(Mockito.any(Class.class), Mockito.anyInt(), Mockito.any(Throwable.class))).thenAnswer(invocation -> invocation.getArguments()[1]);
        stdValueInstantiator.configureFromIntCreator(creator);
        Object result = stdValueInstantiator.createFromInt(context, 1);
        assertNull(result);
    }

    /*
     * Test for createFromLong() method with no creator
     */
    @Test
    public void testCreateFromLongNoCreator() throws IOException {
        long value = 1;
        Object result = stdValueInstantiator.createFromLong(context, value);
        assertNull(result);
    }

    /*
     * Test for createFromLong() method with long creator
     */
    @Test
    public void testCreateFromLongWithLongCreator() throws IOException {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        when(context.handleInstantiationProblem(Mockito.any(Class.class), Mockito.anyLong(), Mockito.any(Throwable.class))).thenAnswer(invocation -> invocation.getArguments()[1]);
        stdValueInstantiator.configureFromLongCreator(creator);
        Object result = stdValueInstantiator.createFromLong(context, 1);
        assertNull(result);
    }

    /*
     * Test for createFromDouble() method with no creator
     */
    @Test
    public void testCreateFromDoubleNoCreator() throws IOException {
        double value = 1.0;
        Object result = stdValueInstantiator.createFromDouble(context, value);
        assertNull(result);
    }

    /*
     * Test for createFromDouble() method with double creator
     */
    @Test
    public void testCreateFromDoubleWithDoubleCreator() throws IOException {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        when(context.handleInstantiationProblem(Mockito.any(Class.class), Mockito.anyDouble(), Mockito.any(Throwable.class))).thenAnswer(invocation -> invocation.getArguments()[1]);
        stdValueInstantiator.configureFromDoubleCreator(creator);
        Object result = stdValueInstantiator.createFromDouble(context, 1.0);
        assertNull(result);
    }

    /*
     * Test for createFromBoolean() method with no creator
     */
    @Test
    public void testCreateFromBooleanNoCreator() throws IOException {
        boolean value = true;
        Object result = stdValueInstantiator.createFromBoolean(context, value);
        assertNull(result);
    }

    /*
     * Test for createFromBoolean() method with boolean creator
     */
    @Test
    public void testCreateFromBooleanWithBooleanCreator() throws IOException {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        when(context.handleInstantiationProblem(Mockito.any(Class.class), Mockito.anyBoolean(), Mockito.any(Throwable.class))).thenAnswer(invocation -> invocation.getArguments()[1]);
        stdValueInstantiator.configureFromBooleanCreator(creator);
        Object result = stdValueInstantiator.createFromBoolean(context, true);
        assertNull(result);
    }

    /*
     * Test for getDelegateCreator() method with no delegate creator
     */
    @Test
    public void testGetDelegateCreatorNoDelegateCreator() {
        assertNull(stdValueInstantiator.getDelegateCreator());
    }

    /*
     * Test for getArrayDelegateCreator() method with no array delegate creator
     */
    @Test
    public void testGetArrayDelegateCreatorNoArrayDelegateCreator() {
        assertNull(stdValueInstantiator.getArrayDelegateCreator());
    }

    /*
     * Test for getDefaultCreator() method with no default creator
     */
    @Test
    public void testGetDefaultCreatorNoDefaultCreator() {
        assertNull(stdValueInstantiator.getDefaultCreator());
    }

    /*
     * Test for getWithArgsCreator() method with no with args creator
     */
    @Test
    public void testGetWithArgsCreatorNoWithArgsCreator() {
        assertNull(stdValueInstantiator.getWithArgsCreator());
    }

    /*
     * Test for getIncompleteParameter() method with no incomplete parameter
     */
    @Test
    public void testGetIncompleteParameterNoIncompleteParameter() {
        assertNull(stdValueInstantiator.getIncompleteParameter());
    }

    /*
     * Test for getDelegateType() method with no delegate type
     */
    @Test
    public void testGetDelegateTypeNoDelegateType() {
        assertNull(stdValueInstantiator.getDelegateType(context));
    }

    /*
     * Test for getArrayDelegateType() method with no array delegate type
     */
    @Test
    public void testGetArrayDelegateTypeNoArrayDelegateType() {
        assertNull(stdValueInstantiator.getArrayDelegateType(context));
    }
}