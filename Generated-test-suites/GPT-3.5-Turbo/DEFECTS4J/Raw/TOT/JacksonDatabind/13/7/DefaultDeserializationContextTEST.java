import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;

    @Before
    public void setUp() {
        context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
    }

    @Test
    public void testFindObjectId_NullId_ReturnsEntryWithKeyNull() {
        // Arrange
        Object id = null;
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<>();
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();

        // Act
        ReadableObjectId result = context.findObjectId(id, gen, resolverType);

        // Assert
        assertNotNull(result);
        assertEquals(result.getKey(), new ObjectIdGenerator.IdKey(null, null, null, null));
    }

    @Test
    public void testFindObjectId_ObjectIdNotSeenBefore_ReturnsEntryWithKeyAndResolver() {
        // Arrange
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<>();
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();

        // Act
        ReadableObjectId result = context.findObjectId(id, gen, resolverType);

        // Assert
        assertNotNull(result);
        assertEquals(result.getKey(), new ObjectIdGenerator.IdKey(null, null, null, null));
        assertEquals(result.getResolver(), resolverType);
    }

    @Test
    public void testCheckUnresolvedObjectId_EnabledUnresolvedObjectIds_FailsIfAnyUnresolvedRefsExist() {
        // Arrange
        context.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        ReadableObjectId unresolvedObjectId = new ReadableObjectId(new ObjectIdGenerator.IdKey("test", null, null, null));
        unresolvedObjectId.getReferringProperties().add(new Referring());
        context.getObjectIds().put(unresolvedObjectId.getKey(), unresolvedObjectId);

        try {
            // Act
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference ex) {
            // Assert
            assertNotNull(ex);
            assertEquals(ex.getMessage(), "Unresolved forward references for: ");
        }
    }

    @Test
    public void testCheckUnresolvedObjectId_DisabledUnresolvedObjectIds_DoesNotThrowException() {
        // Arrange
        context.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);

        // Act
        context.checkUnresolvedObjectId();

        // Assert
        // No exception thrown
    }

    @Test
    public void testDeserializerInstance_DeserDefIsNull_ReturnsNull() {
        // Arrange
        Annotated ann = new Annotated();
        Object deserDef = null;

        // Act
        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);

        // Assert
        assertNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstance_DeserDefIsInvalidClass_ThrowsIllegalStateException() {
        // Arrange
        Annotated ann = new Annotated();
        Object deserDef = new Object();

        // Act
        context.deserializerInstance(ann, deserDef);
    }

    @Test
    public void testDeserializerInstance_DeserDefIsJsonDeserializer_ReturnsDeserDef() {
        // Arrange
        Annotated ann = new Annotated();
        Object deserDef = new JsonDeserializer<>();

        // Act
        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);

        // Assert
        assertNotNull(result);
        assertEquals(result, deserDef);
    }

    @Test
    public void testKeyDeserializerInstance_DeserDefIsNull_ReturnsNull() {
        // Arrange
        Annotated ann = new Annotated();
        Object deserDef = null;

        // Act
        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);

        // Assert
        assertNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstance_DeserDefIsInvalidClass_ThrowsIllegalStateException() {
        // Arrange
        Annotated ann = new Annotated();
        Object deserDef = new Object();

        // Act
        context.keyDeserializerInstance(ann, deserDef);
    }

    @Test
    public void testKeyDeserializerInstance_DeserDefIsJsonDeserializer_ReturnsDeserDef() {
        // Arrange
        Annotated ann = new Annotated();
        Object deserDef = new KeyDeserializer();

        // Act
        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);

        // Assert
        assertNotNull(result);
        assertEquals(result, deserDef);
    }

    @Test
    public void testCopy_SubclassOverride_ReturnsSubclassInstanceOfDefaultDeserializationContext() {
        // Arrange
        DefaultDeserializationContext originalContext = new DefaultDeserializationContext.Impl(new DeserializerFactory()) {
            @Override
            public DefaultDeserializationContext copy() {
                return new SubclassInstanceOfDefaultDeserializationContext(this);
            }
        };

        // Act
        DefaultDeserializationContext result = originalContext.copy();

        // Assert
        assertTrue(result instanceof SubclassInstanceOfDefaultDeserializationContext);
    }
}

class SubclassInstanceOfDefaultDeserializationContext extends DefaultDeserializationContext {

    public SubclassInstanceOfDefaultDeserializationContext(DefaultDeserializationContext src) {
        super(src);
    }

    @Override
    public DefaultDeserializationContext createInstance(DeserializationConfig config, JsonParser jp, InjectableValues values) {
        // TODO: Implement createInstance() method in subclass
        return null;
    }

    @Override
    public DefaultDeserializationContext with(DeserializerFactory factory) {
        // TODO: Implement with() method in subclass
        return null;
    }
}

class Annotated extends Annotation { }