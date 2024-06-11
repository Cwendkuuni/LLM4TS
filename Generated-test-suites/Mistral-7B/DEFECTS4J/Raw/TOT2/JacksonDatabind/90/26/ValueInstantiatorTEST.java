import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ValueInstantiatorTest {

    @Test
    public void testValueInstantiatorCanInstantiate() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
        assertTrue(valueInstantiator.canInstantiate());
    }

    @Test(expected = IllegalStateException.class)
    public void testValueInstantiatorCannotCreateFromNull() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
        DeserializationContext context = new TestContext();
        valueInstantiator.createUsingDefault(context, null);
    }

    @Test
    public void testValueInstantiatorCreateFromString() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
        DeserializationContext context = new TestContext();
        String value = "test";
        Object result = valueInstantiator.createFromString(context, value);
        assertEquals("test", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueInstantiatorCreateFromInvalidType() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(Integer.class);
        DeserializationContext context = new TestContext();
        String value = "test";
        valueInstantiator.createFromString(context, value);
    }

    @Test
    public void testValueInstantiatorCreateFromObjectWith() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
                assertEquals("arg1", args[0]);
                return "result";
            }
        };
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString("{\"arg1\":\"arg2\"}");
            }
        });
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        DeserializationContext context = mapper.getFactory().createContext();
        Object result = valueInstantiator.createFromObjectWith(context, new Object[]{"arg2"});
        assertEquals("result", result);
    }

    private static class TestContext extends DeserializationContext {
        public TestContext() {
            super(null, null, null, null, null, null, null);
        }
    }
}