import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.SetterlessProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class SetterlessPropertyTest {
    
    private static final JsonParser FAKE_PARSER = new FakeJsonParser();
    private static final DeserializationContext FAKE_CONTEXT = new FakeDeserializationContext();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private SetterlessProperty property;
    private TestClass testInstance;

    @Before
    public void setUp() {
        Annotations annotations = FAKE_PARSER.getParser().getTokenLocation();
      
        NullValueProvider nullValueProvider = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                return null;
            }
        };
      
        TypeDeserializer typeDeserializer = MAPPER.getTypeFactory().buildType(TestClass.class).getTypeHandler();

        property = new SetterlessProperty(null, MAPPER.constructType(TestClass.class), 
            typeDeserializer, annotations, new AnnotatedMethod(TestClass.class));
      
        testInstance = new TestClass();
    }
  
    @Test
    public void testDeserializeAndSet_ValueNull_ReturnsNull() throws Exception {
        testInstance.setField(null);
        property.deserializeAndSet(FAKE_PARSER, FAKE_CONTEXT, testInstance);
        assertEquals(null, testInstance.getField());
    }

    @Test
    public void testDeserializeAndSet_ValueTypeDeserializerNotNull_ThrowsException() throws Exception {
        TypeDeserializer typeDeserializer = MAPPER.getTypeFactory().buildType(String.class).getTypeHandler();
      
        property.setValueTypeDeserializer(typeDeserializer);
      
        try {
            property.deserializeAndSet(FAKE_PARSER, FAKE_CONTEXT, testInstance);
            fail("Expected exception to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Problem deserializing 'setterless' property (\"field\"): " +
                "no way to handle typed deser with setterless yet", e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet_OK() throws Exception {
        String fieldValue = "test";
        testInstance.setField(fieldValue);
      
        property.deserializeAndSet(FAKE_PARSER, FAKE_CONTEXT, testInstance);
      
        assertEquals(fieldValue, testInstance.getField());
    }

    @Test
    public void testDeserializeAndSet_GetterThrowsException_ThrowsException() throws Exception {
        testInstance = new TestClass() {
            @Override
            public String getField() {
                throw new RuntimeException("Test Exception");
            }
        };
      
        try {
            property.deserializeAndSet(FAKE_PARSER, FAKE_CONTEXT, testInstance);
            fail("Expected exception to be thrown");
        } catch (RuntimeException e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet_ToModifyIsNull_ThrowsException() throws Exception {
        property.deserializeAndSet(FAKE_PARSER, FAKE_CONTEXT, testInstance);
      
        assertEquals("Problem deserializing 'setterless' property 'field': get method returned null",
            FAKE_CONTEXT.getPath().get(0).getFrom());
    }

    @Test
    public void testDeserializeSetAndReturn_OK() throws Exception {
        String fieldValue = "test";
      
        testInstance.setField(fieldValue);
      
        Object result = property.deserializeSetAndReturn(FAKE_PARSER, FAKE_CONTEXT, testInstance);
      
        assertEquals(fieldValue, testInstance.getField());
        assertEquals(testInstance, result);
    }

    @Test
    public void testSet_NotSupported_ThrowsException() throws Exception {
        try {
            property.set(testInstance, null);
            fail("Expected exception to be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should never call `set()` on setterless property ('field')", e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn_NotSupported_ThrowsException() throws Exception {
        try {
            property.setAndReturn(testInstance, null);
            fail("Expected exception to be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should never call `set()` on setterless property ('field')", e.getMessage());
        }
    }

    private static class FakeJsonParser extends JsonParser {
        private JsonParser jsonParser;

        public FakeJsonParser() {
            super(null);
            jsonParser = createJsonParser("");
        }

        @Override
        public String getText() throws IOException {
            return null;
        }

        @Override
        public char[] getTextCharacters() throws IOException {
            return new char[0];
        }

        @Override
        public int getTextLength() throws IOException {
            return 0;
        }

        @Override
        public int getTextOffset() throws IOException {
            return 0;
        }

        @Override
        public JsonParser skipChildren() throws IOException {
            return null;
        }

        @Override
        public JsonToken getCurrentToken() {
            return null;
        }

        @Override
        public JsonParser setCurrentValue(Object v) {
            return null;
        }

        @Override
        public String getCurrentName() {
            return null;
        }

        @Override
        public JsonLocation getTokenLocation() {
            return null;
        }

        @Override
        public JsonLocation getCurrentLocation() {
            return null;
        }

        @Override
        public JsonParser enable(JsonParser.Feature f) {
            return null;
        }

        @Override
        public JsonParser disable(JsonParser.Feature f) {
            return null;
        }

        @Override
        public boolean isEnabled(JsonParser.Feature f) {
            return false;
        }

        @Override
        public int getFeatureMask() {
            return 0;
        }

        @Override
        public JsonParser setFeatureMask(int mask) {
            return null;
        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public boolean isClosed() {
            return false;
        }

        @Override
        public JsonStreamContext getParsingContext() {
            return null;
        }
    }

    private static class FakeDeserializationContext extends DeserializationContext {
        private DeserializationContext deserializationContext = new DeserializationContext();

        @Override
        public DeserializationConfig getConfig() {
            return null;
        }

        @Override
        public Object getAttribute(Object key) {
            return null;
        }

        @Override
        public DeserializationContext setAttribute(Object key, Object value) {
            return null;
        }

        @Override
        public JsonParser getParser() {
            return null;
        }

        @Override
        public JsonGenerator getGenerator() {
            return null;
        }

        @Override
        public Object getAttribute(Object key, Object defaultValue) {
            return null;
        }

        @Override
        public Object setAttribute(Object key, Object value, Object defaultValue) {
            return null;
        }

        @Override
        public void addBeanContext(Object bean) {

        }

        @Override
        public void addBean(Contextual bean) {

        }

        @Override
        public void removeBean(Contextual bean) {

        }

        @Override
        public final Class<?> getBeanClass() {
            return null;
        }

        @Override
        public final Annotation getAnnotations() {
            return null;
        }
    }

    private static class TestClass {
        private String field;
      
        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }
}