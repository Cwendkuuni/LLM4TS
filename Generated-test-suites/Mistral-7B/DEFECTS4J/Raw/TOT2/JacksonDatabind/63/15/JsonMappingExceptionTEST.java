import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionBasic() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.readValue("{\"name\": [1]}", String[].class);
        } catch (JsonMappingException e) {
            assertEquals("Root path '': method 'mapper.readValue' and classes java.lang.String[] and [Ljava.lang.String;' have incompatible types", e.getMessage());
            assertNotNull(e.getPath());
            assertEquals(1, e.getPath().size());
            Reference ref = e.getPath().get(0);
            assertEquals("mapper", ref.getFrom().getClass().getName());
            assertNull(ref.getFieldName());
            assertNull(ref.getIndex());
        }
    }

    @Test
    public void testJsonMappingExceptionWithSerialization() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValueAsString(new Object() {
                @JsonIgnore
                String field;
            });
        } catch (JsonMappingException e) {
            assertEquals("Root path '': method 'writeValueAsString' and classes com.fasterxml.jackson.databind.JsonMappingException$Reference and class java.lang.Object have incompatible types", e.getMessage());
            assertNotNull(e.getPath());
            assertEquals(1, e.getPath().size());
            Reference ref = e.getPath().get(0);
            assertEquals("writeValueAsString", ref.getFrom().getClass().getMethod("writeValueAsString").getName());
            assertNull(ref.getFieldName());
            assertNull(ref.getIndex());
        }
    }

    @Test
    public void testJsonMappingExceptionWithFieldReference() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Class<Person> clazz = Person.class;
            mapper.readValue("{\"name\": {\"first\": \"John\", \"last\": \"Doe\"}}", clazz);
            Person person = mapper.readValue("{\"name\": null}", clazz);
            person.setName(null);
            mapper.readValue("{\"name\": null}", clazz);
        } catch (JsonMappingException e) {
            assertEquals("Root path '': method 'readValue' and classes com.fasterxml.jackson.databind.JsonMappingException$Reference and com.fasterxml.jackson.databind.JsonMappingException$Reference have incompatible types", e.getMessage());
            assertNotNull(e.getPath());
            assertEquals(3, e.getPath().size());
            Reference ref1 = e.getPath().get(0);
            assertEquals("readValue", ref1.getFrom().getClass().getMethod("readValue").getName());
            assertEquals("name", ref1.getFieldName());
            assertNull(ref1.getIndex());

            Reference ref2 = e.getPath().get(1);
            assertEquals("readValue", ref2.getFrom().getClass().getMethod("readValue").getName());
            assertEquals("name", ref2.getFieldName());
            assertEquals("first", ref2.getFieldName());
            assertNull(ref2.getIndex());

            Reference ref3 = e.getPath().get(2);
            assertEquals("readValue", ref3.getFrom().getClass().getMethod("readValue").getName());
            assertEquals("name", ref3.getFieldName());
            assertEquals("last", ref3.getFieldName());
            assertNull(ref3.getIndex());
        }
    }

    @Test
    public void testJsonMappingExceptionWithIndexReference() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Person> list = new ArrayList<>();
            list.add(new Person("John", "Doe"));
            mapper.writeValueAsString(list);
            list.set(0, null);
            mapper.writeValueAsString(list);
        } catch (JsonMappingException e) {
            assertEquals("Root path '': method 'writeValueAsString' and classes com.fasterxml.jackson.databind.JsonMappingException$Reference and java.util.ArrayList and [Ljava.lang.Object; have incompatible types", e.getMessage());
            assertNotNull(e.getPath());
            assertEquals(3, e.getPath().size());
            Reference ref1 = e.getPath().get(0);
            assertEquals("writeValueAsString", ref1.getFrom().getClass().getMethod("writeValueAsString").getName());
            assertNull(ref1.getFieldName());
            assertNull(ref1.getIndex());

            Reference ref2 = e.getPath().get(1);
            assertEquals("writeValueAsString", ref2.getFrom().getClass().getMethod("writeValueAsString").getName());
            assertNull(ref2.getFieldName());
            assertEquals(0, ref2.getIndex());

            Reference ref3 = e.getPath().get(2);
            assertEquals("writeValueAsString", ref3.getFrom().getClass().getMethod("writeValueAsString").getName());
            assertNull(ref3.getFieldName());
            assertEquals("list", ref3.getFrom().getClass().getName());
            assertNull(ref3.getIndex());
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithNestedException() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.readValue("{\"name\": null}", Person.class);
        } catch (JsonProcessingException e) {
            throw new JsonMappingException(e, "message");
        }
    }

    @Test
    public void testJsonMappingExceptionWithNullPath() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.readValue("{}", String.class);
        } catch (JsonMappingException e) {
            assertEquals("Root path null", e.getMessage());
            assertNull(e.getPath());
        }
    }

    static class Person {
        String firstName;
        String lastName;

        public Person(String first, String last) {
            this.firstName = first;
            this.lastName = last;
        }

        public String getName() {
            return firstName + " " + lastName;
        }

        public void setName(String name) {
            String[] nameParts = name.split(" ");
            firstName = nameParts[0];
            lastName = nameParts[1];
        }
    }
}