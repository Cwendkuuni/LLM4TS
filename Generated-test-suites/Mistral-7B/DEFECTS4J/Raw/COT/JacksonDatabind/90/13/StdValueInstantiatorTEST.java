package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Test cases for the StdValueInstantiator class.
 */
public class StdValueInstantiatorTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @JsonDeserialize(using = StdValueInstantiator.class)
    static class TestClass1 {
        private String str;
        private int intVal;
        private long longVal;
        private double doubleVal;
        private boolean booleanVal;

        public TestClass1(String str, int intVal, long longVal, double doubleVal, boolean booleanVal) {
            this.str = str;
            this.intVal = intVal;
            this.longVal = longVal;
            this.doubleVal = doubleVal;
            this.booleanVal = booleanVal;
        }

        @Override
        public String toString() {
            return "TestClass1{" +
                    "str='" + str + '\'' +
                    ", intVal=" + intVal +
                    ", longVal=" + longVal +
                    ", doubleVal=" + doubleVal +
                    ", booleanVal=" + booleanVal +
                    '}';
        }
    }

    @Test
    public void testBasicFunctionality() throws IOException {
        StdValueInstantiator instantiator = mapper.getTypeFactory()
                .constructValueType(TestClass1.class, new String[]{"str", "intVal", "longVal", "doubleVal", "booleanVal"});
        StdValueInstantiator instantiatorWithArgs = mapper.getTypeFactory()
                .constructValueType(TestClass1.class, new String[]{"str", "intVal", "longVal", "doubleVal", "booleanVal"},
                        new Class[]{String.class, int.class, long.class, double.class, boolean.class});

        TestClass1 instance1 = instantiator.createUsingDefault(mapper.getContext());
        TestClass1 instance2 = instantiatorWithArgs.createFromObjectWith(mapper.getContext(), new Object[]{"test", 1, 2, 3.0, false});
        assertEquals(instance1.toString(), instance2.toString());
    }

    @Test
    public void testStringConstructor() throws IOException {
        StdValueInstantiator stringConstructor = mapper.getTypeFactory()
                .constructValueType(TestClass1.class, TestClass1.class);
        TestClass1 instance = (TestClass1) stringConstructor.createFromString(mapper.getContext(), "{\"str\":\"test\",\"intVal\":1,\"longVal\":2," +
                "\"doubleVal\":3.0,\"booleanVal\":false}");
        assertEquals("TestClass1{str='test', intVal=1, longVal=2, doubleVal=3.0, booleanVal=false}", instance.toString());
    }

    @Test
    public void testIntConstructor() throws IOException {
        StdValueInstantiator intConstructor = mapper.getTypeFactory()
                .constructValueType(TestClass1.class, TestClass1.class);
        TestClass1 instance = (TestClass1) intConstructor.createFromInt(mapper.getContext(), 1);
        assertEquals("TestClass1{str=null, intVal=1, longVal=0, doubleVal=0.0, booleanVal=false}", instance.toString());

        TestClass1 instanceWithArgs = (TestClass1) intConstructor.createFromObjectWith(mapper.getContext(), new Object[]{1, "test", 2.0, false, 3L});
        assertEquals("TestClass1{str='test', intVal=1, longVal=3, doubleVal=2.0, booleanVal=false}", instanceWithArgs.toString());
    }

    @Test
    public void testLongConstructor() throws IOException {
        StdValueInstantiator longConstructor = mapper.getTypeFactory()
                .constructValueType(TestClass1.class, TestClass1.class);
        TestClass1 instance = (TestClass1) longConstructor.createFromLong(mapper.getContext(), 1);
        assertEquals("TestClass1{str=null, intVal=0, longVal=1, doubleVal=0.0, booleanVal=false}", instance.toString());

        TestClass1 instanceWithArgs = (TestClass1) longConstructor.createFromObjectWith(mapper.getContext(), new Object[]{1L, "test", 2.0, false, 3});
        assertEquals("TestClass1{str='test', intVal=0, longVal=1, doubleVal=2.0, booleanVal=false}", instanceWithArgs.toString());
    }

    @Test
    public void testDoubleConstructor() throws IOException {
        StdValueInstantiator doubleConstructor = mapper.getTypeFactory()
                .constructValueType(TestClass1.class, TestClass1.class);
        TestClass1 instance = (TestClass1) doubleConstructor.createFromDouble(mapper.getContext(), 1.0);
        assertEquals("TestClass1{str=null, intVal=0, longVal=0, doubleVal=1.0, booleanVal=false}", instance.toString());

        TestClass1 instanceWithArgs = (TestClass1) doubleConstructor.createFromObjectWith(mapper.getContext(), new Object[]{1.0, "test", 2, false, 3L});
        assertEquals("TestClass1{str='test', intVal=0, longVal=3, doubleVal=1.0, booleanVal=false}", instanceWithArgs.toString());
    }

    @Test
    public void testBooleanConstructor() throws IOException {
        StdValueInstantiator booleanConstructor = mapper.getTypeFactory()
                .constructValueType(TestClass1.class, TestClass1.class);
        TestClass1 instance = (TestClass1) booleanConstructor.createFromBoolean(mapper.getContext(), true);
        assertEquals("TestClass1{str=null, intVal=0, longVal=0, doubleVal=0.0, booleanVal=true}", instance.toString());

        TestClass1 instanceWithArgs = (TestClass1) booleanConstructor.createFromObjectWith(mapper.getContext(), new Object[]{true, "test", 2.0, false, 3L});
        assertEquals("TestClass1{str='test', intVal=0, longVal=3, doubleVal=2.0, booleanVal=true}", instanceWithArgs.toString());
    }

    @Test
    public void testCanCreateUsingDefault() throws IOException {
        StdValueInstantiator instantiator = mapper.getTypeFactory()
                .constructValueType(TestClass1.class, new String[]{"str", "intVal", "longVal", "doubleVal", "booleanVal"});
        assertTrue(instantiator.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateUsingDelegate() throws IOException {
        StdValueInstantiator instantiator = mapper.getTypeFactory()
                .constructValueType(TestClass1.class, new String[]{"str", "intVal", "longVal", "doubleVal", "booleanVal"},
                        new Class[]{String.class, int.class, long.class, double.class, boolean.class});
        assertTrue(instantiator.canCreateUsingDelegate());
    }

    @Test
    public void testCanCreateUsingArrayDelegate() throws IOException {
        StdValueInstantiator arrayDelegateInstantiator = mapper.getTypeFactory()
                .constructValueType(TestClass1[].class, new String[]{"str", "intVal", "longVal", "doubleVal", "booleanVal"},
                        new Class[]{TestClass1.class, int.class, long.class, double.class, boolean.class});
        assertTrue(arrayDelegateInstantiator.canCreateUsingArrayDelegate());
    }

    @Test
    public void testCanCreateFromObjectWith() throws IOException {
        StdValueInstantiator instantiator = mapper.getTypeFactory()
                .constructValueType(TestClass1.class, new String[]{"str", "intVal", "longVal", "doubleVal", "booleanVal"});
        assertTrue(instantiator.canCreateFromObjectWith());
    }

    @Test(expected = JsonMappingException.class)
    public void testExceptionHandling() throws IOException {
        StdValueInstantiator instantiator = mapper.getTypeFactory()
                .constructValueType(TestClass1.class, new String[]{"str", "intVal", "longVal", "doubleVal", "booleanVal"});
        instantiator.createUsingDefault(mapper.getContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testNullValue() throws IOException {
        StdValueInstantiator instantiator = mapper.getTypeFactory()
                .constructValueType(TestClass1.class, new String[]{"str", "intVal", "longVal", "doubleVal", "booleanVal"});
        instantiator.createFromObjectWith(mapper.getContext(), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidConstructorArgs() throws IOException {
        StdValueInstantiator instantiator = mapper.getTypeFactory()
                .constructValueType(TestClass1.class, new String[]{"str", "intVal", "longVal", "doubleVal", "booleanVal"});
        instantiator.createFromObjectWith(mapper.getContext(), new Object[]{1, 2.0, "test", false, 3L});
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDelegateCreator() throws IOException {
        StdValueInstantiator instantiator = mapper.getTypeFactory()
                .constructValueType(TestClass1.class, new String[]{"str", "intVal", "longVal", "doubleVal", "booleanVal"},
                        new Class[]{Integer.class, String.class, Double.class, Boolean.class, Long.class});
        instantiator.createUsingDelegate(mapper.getContext(), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDelegateArguments() throws IOException {
        StdValueInstantiator instantiator = mapper.getTypeFactory()
                .constructValueType(TestClass1.class, new String[]{"str", "intVal", "longVal", "doubleVal", "booleanVal"},
                        new Class[]{String.class, int.class, long.class, double.class, boolean.class});
        instantiator.createUsingArrayDelegate(mapper.getContext(), new Object[]{"test", 1, 2.0, false, null});
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDefaultCreator() throws IOException {
        StdValueInstantiator instantiator = mapper.getTypeFactory()
                .constructValueType(TestClass1.class, new String[]{"str", "intVal", "longVal", "doubleVal", "booleanVal"});
        instantiator.configureFromObjectSettings(null, null, null, null, null, null);
        instantiator.createUsingDefault(mapper.getContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDelegateArgumentsLength() throws IOException {
        StdValueInstantiator instantiator = mapper.getTypeFactory()
                .constructValueType(TestClass1.class, new String[]{"str", "intVal", "longVal", "doubleVal", "booleanVal"},
                        new Class[]{String.class, int.class, long.class, double.class, boolean.class});
        instantiator.configureFromArraySettings(null, null, null);
        instantiator.createUsingDelegate(mapper.getContext(), new Object[]{1, "test", 2.0, false, 3L, 5});
    }
}