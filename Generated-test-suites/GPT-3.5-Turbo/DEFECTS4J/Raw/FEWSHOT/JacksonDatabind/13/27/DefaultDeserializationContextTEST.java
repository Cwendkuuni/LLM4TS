import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class DefaultDeserializationContextTest {
    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext.Impl deserializationContext = mock(DefaultDeserializationContext.Impl.class);
        ObjectIdGenerator<?> idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();
        Object id = new Object();
        when(deserializationContext.findObjectId(id, idGenerator, resolverType)).thenReturn(null);

        assertNull(deserializationContext.findObjectId(id, idGenerator, resolverType));
    }

    @Test
    public void testFindObjectIdWithIdNull() {
        DefaultDeserializationContext.Impl deserializationContext = mock(DefaultDeserializationContext.Impl.class);
        ObjectIdGenerator<?> idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();
        Object id = null;
        when(deserializationContext.findObjectId(id, idGenerator, resolverType)).thenReturn(null);

        assertNull(deserializationContext.findObjectId(id, idGenerator, resolverType));
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext.Impl deserializationContext = mock(DefaultDeserializationContext.Impl.class);

        when(deserializationContext.checkUnresolvedObjectId()).thenCallRealMethod();

        deserializationContext.checkUnresolvedObjectId();

        verify(deserializationContext, atLeastOnce()).checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext.Impl deserializationContext = mock(DefaultDeserializationContext.Impl.class);
        Annotated annotated = mock(Annotated.class);
        Object deserDef = mock(Object.class);

        assertNull(deserializationContext.deserializerInstance(annotated, deserDef));
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext.Impl deserializationContext = mock(DefaultDeserializationContext.Impl.class);
        Annotated annotated = mock(Annotated.class);
        Object deserDef = mock(Object.class);

        assertNull(deserializationContext.keyDeserializerInstance(annotated, deserDef));
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext.Impl deserializationContext = mock(DefaultDeserializationContext.Impl.class);
        DeserializerFactory factory = mock(DeserializerFactory.class);

        when(deserializationContext.with(factory)).thenCallRealMethod();

        deserializationContext.with(factory);

        verify(deserializationContext, atLeastOnce()).with(factory);
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext.Impl deserializationContext = mock(DefaultDeserializationContext.Impl.class);
        DeserializationConfig config = mock(DeserializationConfig.class);
        JsonParser jp = mock(JsonParser.class);
        InjectableValues values = mock(InjectableValues.class);

        when(deserializationContext.createInstance(config, jp, values)).thenCallRealMethod();

        deserializationContext.createInstance(config, jp, values);

        verify(deserializationContext, atLeastOnce()).createInstance(config, jp, values);
    }
}