package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * This class tests the functionality of ExternalTypeHandler class.
 */
public class ExternalTypeHandlerTest {

    @Test
    public void testExternalTypeHandler() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonNode sourceNode = mapper.readTree("{\"name\":\"John\",\"type\":\"Person\",\"age\":null,\"address\":null}");
        Map<String, SettableBeanProperty> properties = new HashMap<>();
        SettableBeanProperty nameProp = mapper.findProperty("Person", "name");
        SettableBeanProperty ageProp = mapper.findProperty("Person", "age");
        SettableBeanProperty addressProp = mapper.findProperty("Person", "address");
        properties.put("name", nameProp);
        properties.put("age", ageProp);
        properties.put("address", addressProp);

        TypeDeserializer ageDeserializer = mapper.constructTypeDeserializer(Integer.class, mapper.getTypeFactory().constructType(Integer.class));
        TypeDeserializer addressDeserializer = mapper.constructTypeDeserializer(Address.class, mapper.getTypeFactory().constructType(Address.class));

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        for (Map.Entry<String, SettableBeanProperty> entry : properties.entrySet()) {
            builder.addExternal(entry.getValue(), entry.getValue().getTypeIdResolver().idFromValueAndType(null, entry.getValue().getType()));
        }
        ExternalTypeHandler externalTypeHandler = builder.build();

        // When
        Object result = externalTypeHandler.complete(mapper.getFactory().createParser(sourceNode.toString()), mapper.getDeserializationContext(), null);

        // Then
        Person person = (Person) result;
        assertEquals("John", person.getName());
        assertNull(person.getAge());
        assertNull(person.getAddress());
    }

    @Test
    public void testExternalTypeHandlerWithNullValues() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonNode sourceNode = mapper.readTree("{\"name\":\"John\",\"type\":\"Person\",\"age\":null,\"address\":null}");
        Map<String, SettableBeanProperty> properties = new HashMap<>();
        SettableBeanProperty nameProp = mapper.findProperty("Person", "name");
        SettableBeanProperty ageProp = mapper.findProperty("Person", "age");
        SettableBeanProperty addressProp = mapper.findProperty("Person", "address");
        properties.put("name", nameProp);
        properties.put("age", ageProp);
        properties.put("address", addressProp);

        TypeDeserializer ageDeserializer = mapper.constructTypeDeserializer(Integer.class, mapper.getTypeFactory().constructType(Integer.class));
        TypeDeserializer addressDeserializer = mapper.constructTypeDeserializer(Address.class, mapper.getTypeFactory().constructType(Address.class));

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        for (Map.Entry<String, SettableBeanProperty> entry : properties.entrySet()) {
            builder.addExternal(entry.getValue(), entry.getValue().getTypeIdResolver().idFromValueAndType(null, entry.getValue().getType()));
        }
        ExternalTypeHandler externalTypeHandler = builder.build();

        // When
        Object result = externalTypeHandler.complete(mapper.getFactory().createParser(sourceNode.toString()), mapper.getDeserializationContext(), null, null);

        // Then
        Person person = (Person) result;
        assertEquals("John", person.getName());
        assertNull(person.getAge());
        assertNull(person.getAddress());
    }

    @Test
    public void testExternalTypeHandlerWithMissingProperties() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonNode sourceNode = mapper.readTree("{\"name\":\"John\",\"type\":\"Person\",\"age\":\"25\"}");
        Map<String, SettableBeanProperty> properties = new HashMap<>();
        SettableBeanProperty nameProp = mapper.findProperty("Person", "name");
        SettableBeanProperty ageProp = mapper.findProperty("Person", "age");
        SettableBeanProperty addressProp = mapper.findProperty("Person", "address");
        properties.put("name", nameProp);
        properties.put("age", ageProp);
        properties.put("address", addressProp);

        TypeDeserializer ageDeserializer = mapper.constructTypeDeserializer(Integer.class, mapper.getTypeFactory().constructType(Integer.class));
        TypeDeserializer addressDeserializer = mapper.constructTypeDeserializer(Address.class, mapper.getTypeFactory().constructType(Address.class));

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        for (Map.Entry<String, SettableBeanProperty> entry : properties.entrySet()) {
            builder.addExternal(entry.getValue(), entry.getValue().getTypeIdResolver().idFromValueAndType(null, entry.getValue().getType()));
        }
        ExternalTypeHandler externalTypeHandler = builder.build();

        // When & Then
        try {
            externalTypeHandler.complete(mapper.getFactory().createParser(sourceNode.toString()), mapper.getDeserializationContext(), null);
            fail("Expected MappingException");
        } catch (MappingException e) {
            // Then: Missing property 'address' for external type id 'address'
            String expectedMsg = "Missing property 'address' for external type id 'address'";
            assertTrue(expectedMsg.contains(e.getMessage()));
        }
    }

    @Test
    public void testExternalTypeHandlerWithMissingTypeIds() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonNode sourceNode = mapper.readTree("{\"name\":\"John\",\"type\":\"Person\",\"age\":null,\"address\":{\"street\":\"Main St\",\"city\":\"New York\"}}");
        Map<String, SettableBeanProperty> properties = new HashMap<>();
        SettableBeanProperty nameProp = mapper.findProperty("Person", "name");
        SettableBeanProperty ageProp = mapper.findProperty("Person", "age");
        SettableBeanProperty addressProp = mapper.findProperty("Person", "address");
        properties.put("name", nameProp);
        properties.put("age", ageProp);
        properties.put("address", addressProp);

        TypeDeserializer ageDeserializer = mapper.constructTypeDeserializer(Integer.class, mapper.getTypeFactory().constructType(Integer.class));
        TypeDeserializer addressDeserializer = mapper.constructTypeDeserializer(Address.class, mapper.getTypeFactory().constructType(Address.class));

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        for (Map.Entry<String, SettableBeanProperty> entry : properties.entrySet()) {
            builder.addExternal(entry.getValue(), entry.getValue().getTypeIdResolver().idFromValueAndType(null, entry.getValue().getType()));
        }
        ExternalTypeHandler externalTypeHandler = builder.build();

        // When & Then
        try {
            externalTypeHandler.complete(mapper.getFactory().createParser(sourceNode.toString()), mapper.getDeserializationContext(), null);
            fail("Expected MappingException");
        } catch (MappingException e) {
            // Then: Missing external type id property 'street'
            String expectedMsg = "Missing external type id property 'street'";
            assertTrue(expectedMsg.contains(e.getMessage()));
        }
    }

    @Test
    public void testExternalTypeHandlerWithNestedObjects() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonNode sourceNode = mapper.readTree("{\"name\":\"John\",\"type\":\"Person\",\"age\":null,\"address\":{\"type\":\"Address\",\"street\":\"Main St.\",\"city\":\"New York\"}}");
        Map<String, SettableBeanProperty> properties = new HashMap<>();
        SettableBeanProperty nameProp = mapper.findProperty("Person", "name");
        SettableBeanProperty ageProp = mapper.findProperty("Person", "age");
        SettableBeanProperty addressProp = mapper.findProperty("Person", "address");
        properties.put("name", nameProp);
        properties.put("age", ageProp);
        properties.put("address", addressProp);

        TypeDeserializer ageDeserializer = mapper.constructTypeDeserializer(Integer.class, mapper.getTypeFactory().constructType(Integer.class));
        TypeDeserializer addressDeserializer = mapper.constructTypeDeserializer(Address.class, mapper.getTypeFactory().constructType(Address.class));

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        for (Map.Entry<String, SettableBeanProperty> entry : properties.entrySet()) {
            builder.addExternal(entry.getValue(), entry.getValue().getTypeIdResolver().idFromValueAndType(null, entry.getValue().getType()));
        }
        ExternalTypeHandler externalTypeHandler = builder.build();

        // When
        Object result = externalTypeHandler.complete(mapper.getFactory().createParser(sourceNode.toString()), mapper.getDeserializationContext(), null);

        // Then
        Person person = (Person) result;
        Address address = person.getAddress();
        assertEquals("Main St.", address.getStreet());
        assertEquals("New York", address.getCity());
        assertNull(person.getAge());
    }

    @Test
    public void testExternalTypeHandlerWithCreator() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonNode sourceNode = mapper.readTree("{\"name\":\"John\",\"type\":\"Person\",\"age\":null,\"address\":{\"type\":\"AddressCreator\",\"street\":\"Main St.\",\"city\":\"New York\"}}");
        Map<String, SettableBeanProperty> properties = new HashMap<>();
        SettableBeanProperty nameProp = mapper.findProperty("Person", "name");
        SettableBeanProperty ageProp = mapper.findProperty("Person", "age");
        SettableBeanProperty addressProp = mapper.findProperty("Person", "address");
        properties.put("name", nameProp);
        properties.put("age", ageProp);
        properties.put("address", addressProp);

        TypeDeserializer ageDeserializer = mapper.constructTypeDeserializer(Integer.class, mapper.getTypeFactory().constructType(Integer.class));
        TypeDeserializer addressDeserializer = mapper.constructTypeDeserializer(Address.class, mapper.getTypeFactory().constructType(Address.class));
        TypeDeserializer addressCreator = mapper.constructDeserializer(AddressCreator.class);

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        for (Map.Entry<String, SettableBeanProperty> entry : properties.entrySet()) {
            builder.addExternal(entry.getValue(), entry.getValue().getTypeIdResolver().idFromValueAndType(null, entry.getValue().getType()));
        }
        ExternalTypeHandler externalTypeHandler = builder.build();

        // When
        Object result = externalTypeHandler.complete(mapper.getFactory().createParser(sourceNode.toString()), mapper.getDeserializationContext(), mapper.createPropertyValueBuffer(), addressCreator);

        // Then
        Person person = (Person) result;
        Address address = person.getAddress();
        assertEquals("Main St.", address.getStreet());
        assertEquals("New York", address.getCity());
        assertNull(person.getAge());
    }

    // Helper classes

    static class Address {
        private String street;
        private String city;

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

    static class AddressCreator implements PropertyBasedCreator<Address> {
        @Override
        public Address createUsingGuid(Object bean, PropertyName propertyName, JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonToken token = p.nextToken();
            if (token.isNull()) {
                return null;
            }
            String street = null;
            String city = null;
            if (token.isStartArray()) {
                p.nextToken();
                street = p.nextTextValue();
                city = p.nextTextValue();
                p.nextToken(JsonToken.END_ARRAY);
            }
            return new Address() {{
                setStreet(street);
                setCity(city);
            }};
        }

        @Override
        public Class<?> getValueInstantiator(DeserializationContext ctxt) {
            return Address.class;
        }
    }
}