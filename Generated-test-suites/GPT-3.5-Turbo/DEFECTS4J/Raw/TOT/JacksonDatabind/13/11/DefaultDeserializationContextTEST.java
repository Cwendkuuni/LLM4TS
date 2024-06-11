import static org.junit.Assert.*;
import org.junit.*;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext deserializationContext;
    private DeserializationConfig config;
    private JsonParser jsonParser;
    private InjectableValues values;

    @Before
    public void setUp() {
        DeserializerFactory factory = new DeserializerFactoryImpl();
        deserializationContext = new DefaultDeserializationContext.Impl(factory);
        config = new DeserializationConfig();
        jsonParser = new JsonParser();
        values = new InjectableValues();
    }

    @Test
    public void testFindObjectId() {
        ObjectIdGenerator generator = new SimpleObjectIdGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        Object id = "123";
        ReadableObjectId objectid = deserializationContext.findObjectId(id, generator, resolver);
        assertEquals(id, objectid.getKey().key);
        assertEquals(resolver, objectid.getResolver());

        // Test when the same id is used
        ReadableObjectId objectid2 = deserializationContext.findObjectId(id, generator, resolver);
        assertEquals(objectid, objectid2);
    }

    @Test
    public void testFindObjectIdWithNull() {
        ObjectIdGenerator generator = new SimpleObjectIdGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        Object id = null;
        ReadableObjectId objectid = deserializationContext.findObjectId(id, generator, resolver);
        assertNull(objectid.getKey().key);
        assertEquals(resolver, objectid.getResolver());
    }

    @Test
    public void testFindObjectIdWithDifferentResolverTypes() {
        ObjectIdGenerator generator = new SimpleObjectIdGenerator();
        ObjectIdResolver resolver1 = new SimpleObjectIdResolver();
        ObjectIdResolver resolver2 = new AnotherObjectIdResolver();
        Object id = "123";
        ReadableObjectId objectid1 = deserializationContext.findObjectId(id, generator, resolver1);
        ReadableObjectId objectid2 = deserializationContext.findObjectId(id, generator, resolver2);
        assertEquals(resolver1, objectid1.getResolver());
        assertEquals(resolver2, objectid2.getResolver());
    }

    @Test
    public void testCheckUnresolvedObjectIdWithNoException() {
        // When no object ids are present and FAIL_ON_UNRESOLVED_OBJECT_IDS is disabled
        assertFalse(deserializationContext.isEnabled(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS));
        deserializationContext.checkUnresolvedObjectId();
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectIdWithException() {
        deserializationContext.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        ObjectIdGenerator generator = new SimpleObjectIdGenerator();
        ObjectIdResolver resolver1 = new SimpleObjectIdResolver();
        Object id = "123";
        ReadableObjectId objectid = deserializationContext.findObjectId(id, generator, resolver1);
        objectid.setReferring(new ReferringClass());

        deserializationContext.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() {
        Annotated ann = new AnnotatedImpl();
        Object deserDef = new JsonDeserializerImpl();
        JsonDeserializer<Object> deserializer = deserializationContext.deserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof JsonDeserializerImpl);
    }

    @Test
    public void testDeserializerInstanceWithNull() {
        Annotated ann = new AnnotatedImpl();
        Object deserDef = null;
        JsonDeserializer<Object> deserializer = deserializationContext.deserializerInstance(ann, deserDef);
        assertNull(deserializer);
    }

    @Test
    public void testDeserializerInstanceWithClass() {
        Annotated ann = new AnnotatedImpl();
        Object deserDef = AnotherJsonDeserializer.class;
        JsonDeserializer<Object> deserializer = deserializationContext.deserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AnotherJsonDeserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithInvalidType() {
        Annotated ann = new AnnotatedImpl();
        Object deserDef = "InvalidDeserDef";
        deserializationContext.deserializerInstance(ann, deserDef);
    }

    @Test
    public void testKeyDeserializerInstance() {
        Annotated ann = new AnnotatedImpl();
        Object deserDef = new KeyDeserializerImpl();
        KeyDeserializer deserializer = deserializationContext.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof KeyDeserializerImpl);
    }

    @Test
    public void testKeyDeserializerInstanceWithNull() {
        Annotated ann = new AnnotatedImpl();
        Object deserDef = null;
        KeyDeserializer deserializer = deserializationContext.keyDeserializerInstance(ann, deserDef);
        assertNull(deserializer);
    }

    @Test
    public void testKeyDeserializerInstanceWithClass() {
        Annotated ann = new AnnotatedImpl();
        Object deserDef = AnotherKeyDeserializer.class;
        KeyDeserializer deserializer = deserializationContext.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AnotherKeyDeserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceWithInvalidType() {
        Annotated ann = new AnnotatedImpl();
        Object deserDef = "InvalidDeserDef";
        deserializationContext.keyDeserializerInstance(ann, deserDef);
    }

}