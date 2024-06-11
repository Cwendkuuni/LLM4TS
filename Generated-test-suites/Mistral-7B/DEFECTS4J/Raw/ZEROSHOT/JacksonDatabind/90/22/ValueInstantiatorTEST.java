package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;

/**
 * Test class for ValueInstantiator abstract class.
 */
public class ValueInstantiatorTest {

    /**
     * Test class for ValueInstantiator abstract class.
     */
    @SuppressWarnings("unchecked")
    public static class TestValueInstantiator extends ValueInstantiator {
        private static final Class<Person> PERSON_CLASS = Person.class;
        private static final JavaType PERSON_TYPE = TypeFactory.defaultInstance().constructType(Person.class);

        @Override
        public boolean canCreateFromString() {
            return true;
        }

        @Override
        public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            Object readValue = mapper.readValue(value, Person.class);
            return readValue;
        }

        @Override
        public boolean canCreateFromInt() {
            return true;
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return new Integer(value);
        }

        @Override
        public boolean canCreateFromLong() {
            return true;
        }

        @Override
        public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
            return new Long(value);
        }

        @Override
        public boolean canCreateFromDouble() {
            return true;
        }

        @Override
        public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
            return new Double(value);
        }

        @Override
        public boolean canCreateFromBoolean() {
            return true;
        }

        @Override
        public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
            return new Boolean(value);
        }

        @Override
        public boolean canCreateUsingDefault() {
            return false;
        }

        @Override
        public boolean canCreateUsingDelegate() {
            return false;
        }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            fail("Should not be called");
            return null;
        }

        @Override
        public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            fail("Should not be called");
            return null;
        }

        @Override
        public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
            return new SettableBeanProperty[0];
        }

        @Override
        public JavaType getDelegateType(DeserializationConfig config) {
            return null;
        }

        @Override
        public JavaType getArrayDelegateType(DeserializationConfig config) {
            return null;
        }

        @Override
        public AnnotatedWithParams getDefaultCreator() {
            return null;
        }

        @Override
        public AnnotatedWithParams getDelegateCreator() {
            return null;
        }

        @Override
        public AnnotatedWithParams getArrayDelegateCreator() {
            return null;
        }

        @Override
        public AnnotatedWithParams getWithArgsCreator() {
            return null;
        }

        @Override
        public AnnotatedParameter getIncompleteParameter() {
            return null;
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            Person person = new Person();
            BeanPropertyDefinition[] properties = Introspector.findSimpleValueAnnotations(Person.class, null);
            for (int i = 0; i < properties.length; i++) {
                BeanPropertyDefinition property = properties[i];
                AnnotatedMember member = property.getMember();
                if (member instanceof AnnotatedConstructor) {
                    AnnotatedConstructor constructor = (AnnotatedConstructor) member;
                    if (constructor.isParameterless()) {
                        Object arg = args[i];
                        Object instantiated = createFromObjectWith(ctxt, property.getParameterName(), arg);
                        person = (Person) instantiateWithArgs(ctxt, property, arg, instantiated);
                        break;
                    }
                }
            }
            return person;
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, String propertyName, Object value)
                throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            Object readValue = mapper.readValue(mapper.writeValueAsString(value), PERSON_TYPE);
            return readValue;
        }

        @Override
        public Object instantiateWithArgs(DeserializationContext ctxt, AnnotatedMember member, Object arg,
                Object instantiated) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            Object value = mapper.readValue(mapper.writeValueAsString(arg), member.getDeclaringClass());
            return value;
        }

        @Override
        public Object createUsingArrayDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            fail("Should not be called");
            return null;
        }

        @Override
        public Object createFromString(DeserializationContext ctxt, String value, Class<?> expectedType)
                throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            Object readValue = mapper.readValue(value, PERSON_TYPE);
            assertEquals(expectedType, readValue.getClass());
            return readValue;
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value, Class<?> expectedType)
                throws IOException {
            assertEquals(Integer.TYPE, expectedType);
            assertEquals(value, new Integer(value).intValue());
            return new Integer(value);
        }

        @Override
        public Object createFromLong(DeserializationContext ctxt, long value, Class<?> expectedType)
                throws IOException {
            assertEquals(Long.TYPE, expectedType);
            assertEquals(value, new Long(value).longValue());
            return new Long(value);
        }

        @Override
        public Object createFromDouble(DeserializationContext ctxt, double value, Class<?> expectedType)
                throws IOException {
            assertEquals(Double.TYPE, expectedType);
            assertEquals(value, new Double(value).doubleValue());
            return new Double(value);
        }

        @Override
        public Object createFromBoolean(DeserializationContext ctxt, boolean value, Class<?> expectedType)
                throws IOException {
            assertEquals(Boolean.TYPE, expectedType);
            assertEquals(value, new Boolean(value).booleanValue());
            return new Boolean(value);
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args, Class<?> expectedType)
                throws IOException {
            Person person = createFromObjectWith(ctxt, args);
            assertEquals(expectedType, person.getClass());
            return person;
        }
    }

    /**
     * Test for ValueInstantiator abstract class.
     */
    public void testValueInstantiator() throws JsonProcessingException, IOException {
        DeserializationConfig config = new DeserializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        mapper.enable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);

        ValueInstantiator instantiator = new TestValueInstantiator(PERSON_TYPE);
        config.setValueInstantiator(instantiator);
        mapper.setConfig(config);

        String jsonString = "{\"name\":\"John\",\"age\":30}";
        Person person = mapper.readValue(jsonString, Person.class);
        assertEquals("John", person.getName());
        assertEquals(30, person.getAge());

        int intValue = 10;
        Person intPerson = mapper.readValue(Ints.toString(intValue), Person.class);
        assertEquals(intValue, intPerson.getAge());

        long longValue = 20L;
        Person longPerson = mapper.readValue(Longs.toString(longValue), Person.class);
        assertEquals(longValue, longPerson.getAge());

        double doubleValue = 30.5;
        Person doublePerson = mapper.readValue(Doubles.toString(doubleValue), Person.class);
        assertEquals(doubleValue, doublePerson.getAge(), 0.001);

        boolean booleanValue = true;
        Person booleanPerson = mapper.readValue(Boolean.toString(booleanValue), Person.class);
        assertTrue(booleanPerson.isAdult());

        String emptyString = "";
        Person emptyStringPerson = mapper.readValue(emptyString, Person.class);
        assertNull(emptyStringPerson);

        String nullString = null;
        Person nullStringPerson = mapper.readValue(nullString, Person.class);
        assertNull(nullStringPerson);

        // Test creating instances from JSON Object
        String jsonObjectString = "{\"name\":\"Mary\",\"age\":45}";
        Person jsonObjectPerson = mapper.readValue(jsonObjectString, Person.class);
        assertEquals("Mary", jsonObjectPerson.getName());
        assertEquals(45, jsonObjectPerson.getAge());

        // Test creating instances from JSON Array
        String jsonArrayString = "[35, {\"name\":\"Tom\",\"age\":25}]";
        ObjectMapper jsonMapper = new ObjectMapper();
        JsonParser jsonParser = jsonMapper.getFactory().createParser(jsonArrayString);
        Object readValue = jsonMapper.readValue(jsonParser, new TypeReference<List<Person>>() {});
        assertEquals(2, readValue.size());
        Person arrayPerson1 = (Person) readValue.get(0);
        assertEquals(35, arrayPerson1.getAge());
        Person arrayPerson2 = (Person) readValue.get(1);
        assertEquals("Tom", arrayPerson2.getName());
        assertEquals(25, arrayPerson2.getAge());
    }

    /**
     * Test for creating instances from String.
     */
    public void testCreateFromString() throws IOException {
        String jsonString = "{\"name\":\"John\",\"age\":30}";
        ValueInstantiator instantiator = new TestValueInstantiator(PERSON_TYPE);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setConfig(new DeserializationConfig().withValueInstantiator(instantiator));
        Person person = mapper.readValue(jsonString, Person.class);
        assertEquals("John", person.getName());
        assertEquals(30, person.getAge());
    }

    /**
     * Test for creating instances from int.
     */
    public void testCreateFromInt() throws IOException {
        int intValue = 10;
        ValueInstantiator instantiator = new TestValueInstantiator(PERSON_TYPE);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setConfig(new DeserializationConfig().withValueInstantiator(instantiator));
        Person intPerson = mapper.readValue(Ints.toString(intValue), Person.class);
        assertEquals(intValue, intPerson.getAge());
    }

    /**
     * Test for creating instances from long.
     */
    public void testCreateFromLong() throws IOException {
        long longValue = 20L;
        ValueInstantiator instantiator = new TestValueInstantiator(PERSON_TYPE);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setConfig(new DeserializationConfig().withValueInstantiator(instantiator));
        Person longPerson = mapper.readValue(Longs.toString(longValue), Person.class);
        assertEquals(longValue, longPerson.getAge());
    }

    /**
     * Test for creating instances from double.
     */
    public void testCreateFromDouble() throws IOException {
        double doubleValue = 30.5;
        ValueInstantiator instantiator = new TestValueInstantiator(PERSON_TYPE);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setConfig(new DeserializationConfig().withValueInstantiator(instantiator));
        Person doublePerson = mapper.readValue(Doubles.toString(doubleValue), Person.class);
        assertEquals(doubleValue, doublePerson.getAge(), 0.001);
    }

    /**
     * Test for creating instances from boolean.
     */
    public void testCreateFromBoolean() throws IOException {
        boolean booleanValue = true;
        ValueInstantiator instantiator = new TestValueInstantiator(PERSON_TYPE);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setConfig(new DeserializationConfig().withValueInstantiator(instantiator));
        Person booleanPerson = mapper.readValue(Boolean.toString(booleanValue), Person.class);
        assertTrue(booleanPerson.isAdult());
    }

    @JsonProperty("name")
    static class Name {
        private String value;

        public Name() {}

        public Name(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @JsonProperty("age")
    static class Age {
        private int value;

        public Age() {}

        public Age(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @JsonCreator
        public static Age create(@JsonProperty("value") int value) {
            return new Age(value);
        }
    }

    static class Person {
        private Name name;
        private Age age;
        private boolean isAdult;

        public Person() {}

        public Person(Name name, Age age) {
            this.name = name;
            this.age = age;
            this.isAdult = age.getValue() >= 18;
        }

        public Name getName() {
            return name;
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Age getAge() {
            return age;
        }

        public void setAge(Age age) {
            this.age = age;
            this.isAdult = age.getValue() >= 18;
        }

        public boolean isAdult() {
            return isAdult;
        }
    }
}