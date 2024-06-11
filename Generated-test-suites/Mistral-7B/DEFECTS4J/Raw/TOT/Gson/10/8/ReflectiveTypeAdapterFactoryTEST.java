import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testReflectiveTypeAdapterFactory() throws InstantiationException, IllegalAccessException {
        // Given
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("name", "John Doe");
        inputMap.put("age", 30);
        inputMap.put("address", new Address("123 Main St", "Anytown", "CA", "12345"));

        // When
        String json = gson.toJson(inputMap);
        Map<String, Object> outputMap = gson.fromJson(json, Map.class);

        // Then
        assertEquals(inputMap, outputMap);

        // Test nested JSON structure
        Address address = new Address("456 Elm St", "Othertown", "NY", "67890");
        inputMap.put("innerObject", new InnerObject("value1", address));
        json = gson.toJson(inputMap);
        outputMap = gson.fromJson(json, Map.class);
        InnerObject innerObject = (InnerObject) outputMap.get("innerObject");
        assertEquals("value1", innerObject.value);
        assertEquals(address, innerObject.address);

        // Test null values
        inputMap.put("name", null);
        inputMap.put("age", null);
        json = gson.toJson(inputMap);
        outputMap = gson.fromJson(json, Map.class);
        assertEquals(inputMap, outputMap);
        assertNull(outputMap.get("name"));
        assertNull(outputMap.get("age"));

        // Test empty JSON
        inputMap = new HashMap<>();
        json = gson.toJson(inputMap);
        outputMap = gson.fromJson(json, Map.class);
        assertEquals(Collections.emptyMap(), outputMap);

        // Test JSON with missing fields
        inputMap.remove("name");
        json = gson.toJson(inputMap);
        outputMap = gson.fromJson(json, Map.class);
        assertNull(outputMap.get("name"));

        // Test JSON with unrecognized fields
        inputMap.put("unknown", "value");
        json = gson.toJson(inputMap);
        try {
            gson.fromJson(json, Map.class);
            fail("Expected JsonSyntaxException");
        } catch (JsonSyntaxException e) {
            // Expected
        }

        // Test JSON with cyclic references
        Object cyclic = new CyclicObject();
        inputMap.put("cyclic", cyclic);
        try {
            gson.toJson(inputMap);
            fail("Expected StackOverflowError");
        } catch (StackOverflowError e) {
            // Expected
        }
    }

    static class Address {
        @JsonAdapter(AddressJsonAdapter.class)
        private final String street;
        @JsonAdapter(AddressJsonAdapter.class)
        private final String city;
        @JsonAdapter(AddressJsonAdapter.class)
        private final String state;
        @JsonAdapter(AddressJsonAdapter.class)
        private final String zip;

        public Address(String street, String city, String state, String zip) {
            this.street = street;
            this.city = city;
            this.state = state;
            this.zip = zip;
        }

        public String getStreet() {
            return street;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public String getZip() {
            return zip;
        }
    }

    static class InnerObject {
        private final String value;
        private final Address address;

        public InnerObject(String value, Address address) {
            this.value = value;
            this.address = address;
        }

        public String getValue() {
            return value;
        }

        public Address getAddress() {
            return address;
        }
    }

    static class CyclicObject {
        private static final CyclicObject instance = new CyclicObject();

        private CyclicObject() {
            this.field = this;
        }

        private CyclicObject field;
    }
}

class AddressJsonAdapter extends TypeAdapter<Address> {
    @Override
    public Address read(JsonReader jsonReader) throws IOException {
        return null; // Not supported
    }

    @Override
    public void write(JsonWriter jsonWriter, Address address) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("street").value(address.getStreet());
        jsonWriter.name("city").value(address.getCity());
        jsonWriter.name("state").value(address.getState());
        jsonWriter.name("zip").value(address.getZip());
        jsonWriter.endObject();
    }
}