package com.example.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class StdValueInstantiatorTest {

    @Test
    public void testStdValueInstantiatorConstructor() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        assertNotNull(instantiator);
        assertEquals("UNKNOWN TYPE", instantiator.getValueTypeDesc());
        assertEquals(Object.class, instantiator.getValueClass());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.getDelegateType());
        assertNull(instantiator.getDelegateCreator());
        assertNull(instantiator.getDelegateArguments());
        assertNull(instantiator.getArrayDelegateType());
        assertNull(instantiator.getArrayDelegateCreator());
        assertNull(instantiator.getArrayDelegateArguments());
        assertNull(instantiator.getFromStringCreator());
        assertNull(instantiator.getFromIntCreator());
        assertNull(instantiator.getFromLongCreator());
        assertNull(instantiator.getFromDoubleCreator());
        assertNull(instantiator.getFromBooleanCreator());
        assertNull(instantiator.getIncompleteParameter());
    }

    @Test
    public void testStdValueInstantiatorWithArgumentsConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getConfig(), mapper.getTypeFactory().constructType(Person.class));
        assertNotNull(instantiator);
        assertEquals("java.lang.Class<com.example.Person>", instantiator.getValueTypeDesc());
        assertEquals(Person.class, instantiator.getValueClass());
        assertNotNull(instantiator.getDefaultCreator());
        assertNull(instantiator.getDelegateType());
        assertNull(instantiator.getDelegateCreator());
        assertNull(instantiator.getDelegateArguments());
        assertNull(instantiator.getArrayDelegateType());
        assertNull(instantiator.getArrayDelegateCreator());
        assertNull(instantiator.getArrayDelegateArguments());
        assertNull(instantiator.getFromStringCreator());
        assertNull(instantiator.getFromIntCreator());
        assertNull(instantiator.getFromLongCreator());
        assertNull(instantiator.getFromDoubleCreator());
        assertNull(instantiator.getFromBooleanCreator());
        assertNull(instantiator.getIncompleteParameter());

        // Test createUsingDefault
        Person person = instantiator.createUsingDefault(mapper.getDeserializationContext());
        assertNotNull(person);
        assertEquals("John Doe", person.getName());
        assertEquals(25, person.getAge());

        // Test createFromObjectWith
        Object[] args = new Object[]{"John Doe", 25};
        Person personFromArguments = instantiator.createFromObjectWith(mapper.getDeserializationContext(), args);
        assertNotNull(personFromArguments);
        assertEquals("John Doe", personFromArguments.getName());
        assertEquals(25, personFromArguments.getAge());

        // Test createUsingDelegate
        JavaType delegateType = mapper.getTypeFactory().constructType(DelegatePerson.class);
        StdValueInstantiator delegateInstantiator = new StdValueInstantiator(mapper.getConfig(), delegateType);
        Object delegate = mapper.readValue(new StringReader("{\"name\":\"Delegate John\", \"age\":30}"), MapperFactory.getSimpleMapper().getTypeFactory().constructType(DelegatePerson.class));
        Person personFromDelegate = instantiator.createUsingDelegate(mapper.getDeserializationContext(), delegate);
        assertNotNull(personFromDelegate);
        assertEquals("Delegate John", personFromDelegate.getName());
        assertEquals(30, personFromDelegate.getAge());

        // Test createUsingArrayDelegate
        StdValueInstantiator arrayDelegateInstantiator = new StdValueInstantiator(mapper.getConfig(), mapper.getTypeFactory().constructType(ArrayDelegatePerson[].class));
        Object arrayDelegate = mapper.readValue(new StringReader("[\n" +
                "  {\"name\":\"ArrayDelegate1\", \"age\":21},\n" +
                "  {\"name\":\"ArrayDelegate2\", \"age\":22},\n" +
                "  {\"name\":\"ArrayDelegate3\", \"age\":23}\n" +
                "]"), new TypeReference<ArrayDelegatePerson[]>() {});
        Person[] personsFromArrayDelegate = arrayDelegateInstantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), arrayDelegate);
        assertNotNull(personsFromArrayDelegate);
        assertEquals(3, personsFromArrayDelegate.length);
        assertEquals("ArrayDelegate1", personsFromArrayDelegate[0].getName());
        assertEquals(21, personsFromArrayDelegate[0].getAge());
        assertEquals("ArrayDelegate2", personsFromArrayDelegate[1].getName());
        assertEquals(22, personsFromArrayDelegate[1].getAge());
        assertEquals("ArrayDelegate3", personsFromArrayDelegate[2].getName());
        assertEquals(23, personsFromArrayDelegate[2].getAge());

        // Test createFromString
        String jsonString = "\"John Doe\"";
        Person personFromString = instantiator.createFromString(mapper.getDeserializationContext(), jsonString);
        assertNotNull(personFromString);
        assertEquals("John Doe", personFromString.getName());
        assertEquals(0, personFromString.getAge());

        // Test createFromInt
        int intValue = 1;
        Person personFromInt = instantiator.createFromInt(mapper.getDeserializationContext(), intValue);
        assertNotNull(personFromInt);
        assertEquals("IntPerson1", personFromInt.getName());
        assertEquals(intValue, personFromInt.getAge());

        // Test createFromLong
        long longValue = 1L;
        Person personFromLong = instantiator.createFromLong(mapper.getDeserializationContext(), longValue);
        assertNotNull(personFromLong);
        assertEquals("LongPerson1", personFromLong.getName());
        assertEquals((int) longValue, personFromLong.getAge());

        // Test createFromDouble
        double doubleValue = 1.0;
        Person personFromDouble = instantiator.createFromDouble(mapper.getDeserializationContext(), doubleValue);
        assertNotNull(personFromDouble);
        assertEquals("DoublePerson1", personFromDouble.getName());
        assertEquals((int) Double.intBitsToInt((double) doubleValue), personFromDouble.getAge());

        // Test createFromBoolean
        boolean booleanValue = true;
        Person personFromBoolean = instantiator.createFromBoolean(mapper.getDeserializationContext(), booleanValue);
        assertNotNull(personFromBoolean);
        assertEquals("BooleanPerson1", personFromBoolean.getName());
        assertEquals((int) (booleanValue ? 1 : 0), personFromBoolean.getAge());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStdValueInstantiatorInvalidConfiguration() {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getConfig(), null);
        instantiator.configureFromObjectSettings(null, null, null, null, null, null);
        instantiator.createUsingDefault(mapper.getDeserializationContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStdValueInstantiatorNullCreator() {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getConfig(), null);
        instantiator.configureFromObjectSettings(null, null, null, null, new AnnotatedWithParams(null), null);
        instantiator.createUsingDefault(mapper.getDeserializationContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStdValueInstantiatorNullArguments() {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getConfig(), null);
        instantiator.configureFromObjectSettings(null, null, null, null, new AnnotatedWithParams(null), null);
        instantiator.createFromObjectWith(mapper.getDeserializationContext(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStdValueInstantiatorNullDelegateType() {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getConfig(), null);
        instantiator.configureFromArraySettings(null, null, null);
        instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStdValueInstantiatorNullDelegateCreator() {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getConfig(), mapper.getTypeFactory().constructType(ArrayDelegatePerson[].class));
        instantiator.configureFromArraySettings(null, null, null);
        instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStdValueInstantiatorNullArgumentsDelegate() {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getConfig(), mapper.getTypeFactory().constructType(ArrayDelegatePerson[].class));
        instantiator.configureFromArraySettings(null, null, null);
        instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), new Object[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testStdValueInstantiatorInstantiationFailure() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getConfig(), mapper.getTypeFactory().constructType(IllegalPerson.class));
        instantiator.configureFromObjectSettings(new AnnotatedWithParams(mapper.getdeserializationContext().findValueHandler(StringDeserializer.class)), null, null, null, null, null);
        instantiator.createUsingDefault(mapper.getDeserializationContext());
    }

    private static class Person {
        private String name;
        private int age;

        public Person() {
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    private static class DelegatePerson {
        private String name;
        private int age;

        public DelegatePerson() {
            // empty constructor for delegate instantiation
        }

        public DelegatePerson(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    private static class ArrayDelegatePerson {
        private String name;
        private int age;

        public ArrayDelegatePerson() {
            // empty constructor for array delegate instantiation
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    private static class IllegalPerson {
        // empty constructor for testing exception handling
    }
}