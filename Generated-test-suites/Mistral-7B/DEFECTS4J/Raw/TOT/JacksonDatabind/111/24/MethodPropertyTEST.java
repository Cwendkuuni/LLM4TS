import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Assert;
import org.junit.Test;

public class MethodPropertyTest {

    @Test
    public void testMethodPropertyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MyClass obj = new MyClass();

        String json = "{\"property1\": 1, \"property2\": null}";
        JsonParser parser = mapper.getFactory().createParser(json);

        MethodProperty property = mapper.findProperty("property1", MyClass.class);

        Object deserializedObject = property.deserializeAndSet(parser, mapper.getConfig(), obj);

        Assert.assertEquals(1, obj.getProperty1());

        property = mapper.findProperty("property2", MyClass.class);

        Object deserializedNullObject = property.deserializeAndSet(parser, mapper.getConfig(), obj);

        Assert.assertNull(obj.getProperty2());

        json = "{\"property1\": null, \"property2\": \"test\"}";
        parser = mapper.getFactory().createParser(json);

        property = mapper.findProperty("property1", MyClass.class);

        Assert.assertNull(deserializedObject = property.deserializeAndSet(parser, mapper.getConfig(), obj));
        Assert.assertNull(obj.getProperty1());

        property = mapper.findProperty("property2", MyClass.class);

        deserializedObject = property.deserializeAndSet(parser, mapper.getConfig(), obj);
        Assert.assertEquals("test", obj.getProperty2());
    }

    @Test
    public void testMethodPropertySerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MyClass obj = new MyClass(1, "test");

        ObjectWriter writer = mapper.writerWithTypeValue();

        String json = writer.writeValueAsString(obj);

        JsonParser parser = mapper.getFactory().createParser(json);

        Object deserializedObject = parser.readValueAs(MyClass.class);

        Assert.assertEquals(obj.getProperty1(), deserializedObject.getProperty1());
        Assert.assertEquals(obj.getProperty2(), deserializedObject.getProperty2());
    }

    @Test(expected = IOException.class)
    public void testMethodPropertyDeserializationNullValueProvider() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        MyClass obj = new MyClass();

        String json = "{\"property\": null}";
        JsonParser parser = mapper.getFactory().createParser(json);

        MethodProperty property = mapper.findProperty("property", MyClass.class);

        property.deserializeAndSet(parser, mapper.getConfig(), obj);
    }

    @Test
    public void testMethodPropertyWithTypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        MyClassWithTypeObj obj = new MyClassWithTypeObj();
        obj.setProperty(new MyType(1, "test"));

        ObjectWriter writer = mapper.writerWithTypeValue();

        String json = writer.writeValueAsString(obj);

        JsonParser parser = mapper.getFactory().createParser(json);

        MethodProperty property = mapper.findProperty("property", MyClassWithTypeObj.class);

        Object deserializedObject = property.deserializeAndSet(parser, mapper.getConfig(), null);

        Assert.assertEquals(obj.getProperty().getClass(), deserializedObject.getClass());
        Assert.assertEquals(obj.getProperty().getValue(), deserializedObject.getValue());
    }

    @Test
    public void testMethodPropertyFixAccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        MyPrivateClass obj = new MyPrivateClass();

        String json = "{\"property\": 1}";
        JsonParser parser = mapper.getFactory().createParser(json);

        MethodProperty property = mapper.findProperty("property", MyPrivateClass.class);

        property.fixAccess(mapper.getConfig());

        Object deserializedObject = property.deserializeAndSet(parser, mapper.getConfig(), obj);

        Assert.assertEquals(1, obj.getProperty());
    }

    static class MyClass {
        private int property1;
        private String property2;

        public MyClass() {
        }

        public MyClass(int property1, String property2) {
            this.property1 = property1;
            this.property2 = property2;
        }

        public int getProperty1() {
            return property1;
        }

        public String getProperty2() {
            return property2;
        }
    }

    static class MyClassWithTypeObj {
        private MyType property;

        public void setProperty(MyType property) {
            this.property = property;
        }

        public MyType getProperty() {
            return property;
        }
    }

    static class MyType {
        private int value;
        private String name;

        public MyType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    static class MyPrivateClass {
        private int property;

        public int getProperty() {
            return property;
        }
    }
}