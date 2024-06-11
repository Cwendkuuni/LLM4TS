import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.MismatchedTypeException;
import com.fasterxml.jackson.databind.jsontype.TypeSerializers;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private SerializationConfig _config;
    private ObjectNode _node;
    private ObjectMapper _mapper;
    private PropertyBuilder _propertyBuilder;

    @Before
    public void setUp() throws Exception {
        _config = new SerializationConfig();
        _mapper = new ObjectMapper(_config);
        _node = (ObjectNode) _mapper.readValue("{}", ObjectNode.class);
    }

    @Test
    public void testSimpleProperty() throws IOException {
        BeanDescription beanDesc = _mapper.findBeanDescription(Person.class, null);
        _propertyBuilder = new PropertyBuilder(_config, beanDesc);
        JavaType propertyType = TypeFactory.defaultInstance().constructType(String.class);
        PropertyWriter writer = _propertyBuilder.buildWriter(new DefaultSerializerProvider(_config), null, propertyType, null, null, null, null, false);

        writer.writeValue(_node, "test");
        assertEquals("test", _node.get("test"));
    }

    @Test
    public void testComplexProperty() throws IOException {
        BeanDescription beanDesc = _mapper.findBeanDescription(Person.class, null);
        _propertyBuilder = new PropertyBuilder(_config, beanDesc);
        JavaType propertyType = TypeFactory.defaultInstance().constructType(Person.class);
        PropertyWriter writer = _propertyBuilder.buildWriter(new DefaultSerializerProvider(_config), null, propertyType, null, null, null, null, false);

        Person person = new Person("test", new Address("test", "test"));
        writer.writeValue(_node, person);

        Map<String, Object> personMap = _mapper.readValue(_node, new TypeReference<Map<String, Object>>(){});
        Person readPerson = (Person) personMap.get("test");

        assertEquals("test", readPerson.getName());
        assertEquals("test", readPerson.getAddress().getStreet());
        assertEquals("test", readPerson.getAddress().getCity());
    }

    @Test(expected = MismatchedTypeException.class)
    public void testMismatchedType() throws IOException {
        BeanDescription beanDesc = _mapper.findBeanDescription(Person.class, null);
        _propertyBuilder = new PropertyBuilder(_config, beanDesc);
        JavaType propertyType = TypeFactory.defaultInstance().constructType(Integer.class);
        PropertyWriter writer = _propertyBuilder.buildWriter(new DefaultSerializerProvider(_config), null, propertyType, null, null, null, null, false);

        writer.writeValue(_node, new Person("test", new Address("test", "test")));
    }

    @Test
    public void testNullSuppression() throws IOException {
        BeanDescription beanDesc = _mapper.findBeanDescription(Person.class, null);
        _propertyBuilder = new PropertyBuilder(_config, beanDesc);
        JavaType propertyType = TypeFactory.defaultInstance().constructType(String.class);
        PropertyWriter writer = _propertyBuilder.buildWriter(new DefaultSerializerProvider(_config), null, propertyType, null, null, null, null, true);

        writer.writeValue(_node, null);
        assertTrue(_node.isMissingNode("test"));
    }

    @Test
    public void testEmptyCollectionSuppression() throws IOException {
        BeanDescription beanDesc = _mapper.findBeanDescription(Person.class, null);
        _propertyBuilder = new PropertyBuilder(_config, beanDesc);
        JavaType propertyType = TypeFactory.defaultInstance().constructType(List.class);
        PropertyWriter writer = _propertyBuilder.buildWriter(new DefaultSerializerProvider(_config), null, propertyType, null, null, null, null, false);

        writer.writeValue(_node, Collections.emptyList());
        assertTrue(_node.isMissingNode("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyNotFound() throws IOException {
        BeanDescription beanDesc = _mapper.findBeanDescription(Person.class, null);
        _propertyBuilder = new PropertyBuilder(_config, beanDesc);
        JavaType propertyType = TypeFactory.defaultInstance().constructType(String.class);
        PropertyWriter writer = _propertyBuilder.buildWriter(new DefaultSerializerProvider(_config), null, propertyType, null, null, null, null, false);

        writer.writeValue(_node, new String("test"));
        _propertyBuilder = new PropertyBuilder(_config, beanDesc);
        PropertyWriter writer2 = _propertyBuilder.buildWriter(new DefaultSerializerProvider(_config), null, propertyType, null, null, null, null, false);

        writer2.writeValue(_node, new String("test2"));
        Object readValue = _mapper.readValue(_node, Person.class);

        assertNull(readValue);
    }

    @Test
    public void testJsonSerializer() throws IOException {
        BeanDescription beanDesc = _mapper.findBeanDescription(Person.class, null);
        _propertyBuilder = new PropertyBuilder(_config, beanDesc);
        JavaType propertyType = TypeFactory.defaultInstance().constructType(PersonSerializer.class);
        PropertyWriter writer = _propertyBuilder.buildWriter(new DefaultSerializerProvider(_config), null, propertyType, null, null, null, null, false);

        writer.writeValue(_node, new Person("test", new Address("test", "test")));

        Map<String, Object> personMap = _mapper.readValue(_node, new TypeReference<Map<String, Object>>(){});
        PersonSerializer reader = (PersonSerializer) personMap.get("test");

        assertEquals("test", reader.getName());
        assertEquals("test", reader.getAddress().getStreet());
        assertEquals("test", reader.getAddress().getCity());
    }

    @Test
    public void testTypeSerializer() throws IOException {
        BeanDescription beanDesc = _mapper.findBeanDescription(Person.class, null);
        _propertyBuilder = new PropertyBuilder(_config, beanDesc);
        JavaType propertyType = TypeFactory.defaultInstance().constructType(TypeSerializers.ListSerializer.class);
        PropertyWriter writer = _propertyBuilder.buildWriter(new DefaultSerializerProvider(_config), null, propertyType, null, null, null, null, false);

        writer.writeValue(_node, new ArrayList<>());

        Map<String, Object> personMap = _mapper.readValue(_node, new TypeReference<Map<String, Object>>(){});
        TypeSerializers.ListSerializer reader = (TypeSerializers.ListSerializer) personMap.get("test");

        assertTrue(reader instanceof TypeSerializers.ArraySerializerBase);
    }

    private static class Person {
        private String name;
        private Address address;

        public Person(String name, Address address) {
            this.name = name;
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public static class Address {
            private String street;
            private String city;

            public Address(String street, String city) {
                this.street = street;
                this.city = city;
            }

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }
        }
    }

    private static class PersonSerializer extends StdSerializer<Person> {
        public PersonSerializer() {
            this(null);
        }

        public PersonSerializer(Class<Person> t) {
            super(t);
        }

        @Override
        public void serialize(Person person, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeFieldName("name");
            jgen.writeString(person.getName());
            jgen.writeFieldName("address");
            Address address = person.getAddress();
            if (address != null) {
                Field streetField = Address.class.getDeclaredField("street");
                streetField.setAccessible(true);
                jgen.writeStartObject();
                jgen.writeFieldName("street");
                jgen.writeString((String) streetField.get(address));
                jgen.writeFieldName("city");
                jgen.writeString(address.getCity());
                jgen.writeEndObject();
            }
        }
    }
}