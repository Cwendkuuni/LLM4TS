import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<TestObject> serializer;

    @Before
    public void setUp() {
        serializer = new TestDateTimeSerializerBase(TestObject.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void testWithFormat() {
        DateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateTimeSerializerBase<TestObject> newSerializer = serializer.withFormat(true, newFormat);
        assertNotNull(newSerializer);
        assertEquals(newFormat, newSerializer._customFormat);
        assertTrue(newSerializer._useTimestamp);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider dummyProvider = new DummySerializerProvider();

        // Test without format overrides
        BeanProperty nullProperty = new BeanProperty() {
            @Override
            public String getName() {
                return "";
            }

            @Override
            public PropertyName getFullName() {
                return new PropertyName("");
            }

            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public PropertyName getWrapperName() {
                return new PropertyName("");
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public Annotated getAnnotated() {
                return null;
            }

            @Override
            public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }
        };
        DateTimeSerializerBase<TestObject> noOverridesSerializer = serializer.createContextual(dummyProvider, nullProperty);
        assertNotNull(noOverridesSerializer);
        assertEquals(serializer, noOverridesSerializer);

        // Test with numeric format override
        BeanProperty numericProperty = new BeanProperty() {
            @Override
            public String getName() {
                return "";
            }

            @Override
            public PropertyName getFullName() {
                return new PropertyName("");
            }

            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public PropertyName getWrapperName() {
                return new PropertyName("");
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public Annotated getAnnotated() {
                return null;
            }

            @Override
            public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }
        };
        DateTimeSerializerBase<TestObject> numericOverrideSerializer = serializer.createContextual(dummyProvider, numericProperty);
        assertNotNull(numericOverrideSerializer);
        assertTrue(numericOverrideSerializer._useTimestamp);
        assertNull(numericOverrideSerializer._customFormat);

        // Test with string format override
        JsonFormat.Value stringFormatOverride = new JsonFormat.Value();
        stringFormatOverride.setShape(JsonFormat.Shape.STRING);
        BeanProperty stringProperty = new BeanProperty() {
            @Override
            public String getName() {
                return "";
            }

            @Override
            public PropertyName getFullName() {
                return new PropertyName("");
            }

            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public PropertyName getWrapperName() {
                return new PropertyName("");
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public Annotated getAnnotated() {
                return null;
            }

            @Override
            public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }
        };
        DateTimeSerializerBase<TestObject> stringOverrideSerializer = serializer.createContextual(dummyProvider, stringProperty);
        assertNotNull(stringOverrideSerializer);
        assertFalse(stringOverrideSerializer._useTimestamp);
        assertNotNull(stringOverrideSerializer._customFormat);

        // Test with pattern format override
        JsonFormat.Value patternFormatOverride = new JsonFormat.Value();
        patternFormatOverride.setShape(JsonFormat.Shape.STRING);
        patternFormatOverride.setPattern("yyyy/MM/dd");
        patternFormatOverride.setLocale(Locale.US);
        patternFormatOverride.setTimeZone(TimeZone.getTimeZone("GMT"));
        BeanProperty patternProperty = new BeanProperty() {
            @Override
            public String getName() {
                return "";
            }

            @Override
            public PropertyName getFullName() {
                return new PropertyName("");
            }

            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public PropertyName getWrapperName() {
                return new PropertyName("");
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public Annotated getAnnotated() {
                return null;
            }

            @Override
            public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }
        };
        DateTimeSerializerBase<TestObject> patternOverrideSerializer = serializer.createContextual(dummyProvider, patternProperty);
        assertNotNull(patternOverrideSerializer);
        assertFalse(patternOverrideSerializer._useTimestamp);

        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        expectedFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        assertNotNull(patternOverrideSerializer._customFormat);
        assertEquals(expectedFormat.toPattern(), patternOverrideSerializer._customFormat.toPattern());
        assertEquals(expectedFormat.getTimeZone(), patternOverrideSerializer._customFormat.getTimeZone());
    }

    @Test
    public void testIsEmpty() {
        TestObject nullObject = null;
        assertTrue(serializer.isEmpty(nullObject));

        TestObject emptyObject = new TestObject(0L);
        assertTrue(serializer.isEmpty(emptyObject));

        TestObject nonEmptyObject = new TestObject(System.currentTimeMillis());
        assertFalse(serializer.isEmpty(nonEmptyObject));
    }

    @Test
    public void testGetSchema() {
        SerializerProvider dummyProvider = new DummySerializerProvider();
        assertNull(serializer.getSchema(dummyProvider, null));

        DateTimeSerializerBase<TestObject> timestampSerializer = new TestDateTimeSerializerBase(TestObject.class, true, null);
        assertNotNull(timestampSerializer.getSchema(dummyProvider, null));

        DateTimeSerializerBase<TestObject> stringSerializer = new TestDateTimeSerializerBase(TestObject.class, false, new SimpleDateFormat("yyyy-MM-dd"));
        assertNotNull(stringSerializer.getSchema(dummyProvider, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper dummyVisitor = new DummyJsonFormatVisitorWrapper();
        DateTimeSerializerBase<TestObject> timestampSerializer = new TestDateTimeSerializerBase(TestObject.class, true, null);
        timestampSerializer.acceptJsonFormatVisitor(dummyVisitor, null);

        DateTimeSerializerBase<TestObject> stringSerializer = new TestDateTimeSerializerBase(TestObject.class, false, new SimpleDateFormat("yyyy-MM-dd"));
        stringSerializer.acceptJsonFormatVisitor(dummyVisitor, null);
    }

    @Test
    public void testSerialize() {
        DummyJsonGenerator dummyGenerator = new DummyJsonGenerator();
        DummySerializerProvider dummyProvider = new DummySerializerProvider();

        TestObject testObject = new TestObject(System.currentTimeMillis());
        try {
            serializer.serialize(testObject, dummyGenerator, dummyProvider);
            assertTrue(dummyGenerator.isCalled);
        } catch (IOException e) {
            fail();
        }
    }
}

class TestObject {

    private long timestamp;

    public TestObject(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

class TestDateTimeSerializerBase extends DateTimeSerializerBase<TestObject> {

    public TestDateTimeSerializerBase(Class<TestObject> type, Boolean useTimestamp, DateFormat customFormat) {
        super(type, useTimestamp, customFormat);
    }

    @Override
    public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
        return new TestDateTimeSerializerBase(getHandledType(), timestamp, customFormat);
    }

    @Override
    protected long _timestamp(TestObject value) {
        return value.getTimestamp();
    }

    @Override
    public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.getTimestamp());
    }
}

class DummySerializerProvider extends SerializerProvider {

    public DummySerializerProvider() {
        super(null, new JsonFactory(), null);
    }
}

class DummyJsonFormatVisitorWrapper extends JsonFormatVisitorWrapper.Base<JsonFormatVisitorWrapper.Base<?>> {

    @Override
    public SerializerProvider getProvider() {
        return new DummySerializerProvider();
    }
}

class DummyJsonGenerator extends JsonGenerator {

    public boolean isCalled = false;

    @Override
    public void writeNumber(int v) throws IOException {
        isCalled = true;
    }

    @Override
    public void close() throws IOException {

    }

    // Other overridden methods...
}