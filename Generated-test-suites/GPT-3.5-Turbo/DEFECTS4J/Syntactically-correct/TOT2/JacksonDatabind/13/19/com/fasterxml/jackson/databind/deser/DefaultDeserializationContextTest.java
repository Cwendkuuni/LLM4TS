import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext.Impl context;

    @Before
    public void setUp() {
        context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
    }

    @Test
    public void testCopy() {
        DefaultDeserializationContext copiedContext = context.copy();
        assertNotNull(copiedContext);
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext newInstance = context.createInstance(new DeserializationConfig(), new JsonParser(), new InjectableValues());
        assertNotNull(newInstance);
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext newContext = context.with(new DeserializerFactory());
        assertNotNull(newContext);
    }

    @Test
    public void testFindObjectId() {
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<>();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        
        ReadableObjectId objectId = context.findObjectId(id, gen, resolver);
        assertNotNull(objectId);
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testDeserializerInstance() {
        Annotated ann = new Annotated();
        Object deserDef = new Object();
        
        try {
            context.deserializerInstance(ann, deserDef);
        } catch (JsonMappingException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testKeyDeserializerInstance() {
        Annotated ann = new Annotated();
        Object deserDef = new Object();
        
        try {
            context.keyDeserializerInstance(ann, deserDef);
        } catch (JsonMappingException e) {
            fail("Should not throw an exception");
        }
    }
}