import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DefaultDeserializationContextTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(objectMapper.getDeserializerFactory());

    @Test
    public void testFindObjectId_nullId_returnNull() {
        assertNull(context.findObjectId(null, null, null));
    }

    @Test
    public void testFindObjectId_existingId_returnReadableObjectId() {
        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey(Object.class, "id", null);
        ReadableObjectId readableObjectId = new ReadableObjectId(idKey);
        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(idKey, readableObjectId);
        
        assertEquals(readableObjectId, context.findObjectId(Object.class, null, null));
    }

    @Test
    public void testFindObjectId_notExistingId_returnNewReadableObjectId() {
        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey(Object.class, "id", null);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context._objectIdResolvers = new ArrayList<>();
        context._objectIdResolvers.add(resolver);
        
        ReadableObjectId readableObjectId = context.findObjectId(Object.class, null, resolver);
        
        assertNotNull(readableObjectId);
        assertEquals(readableObjectId, context._objectIds.get(idKey));
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId_whenFailOnUnresolvedObjectIdsEnabled_throwException() {
        context.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey(Object.class, "id", null);
        ReadableObjectId readableObjectId = new ReadableObjectId(idKey);
        readableObjectId.appendReferring(new ReadableObjectId.Referring(readableObjectId, Object.class, "fieldName"));
        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(idKey, readableObjectId);
        
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance_nullDeserDef_returnNull() throws Exception {
        assertNull(context.deserializerInstance(null, null));
    }

    @Test
    public void testDeserializerInstance_deserDefIsClass_returnNewInstance() throws Exception {
        Class<?> deserDef = ClassUtil.findClass("com.fasterxml.jackson.databind.deser.BeanDeserializer");
        assertNotNull(context.deserializerInstance(null, deserDef));
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstance_deserDefIsNotClass_throwException() throws Exception {
        context.deserializerInstance(null, new ObjectMapper());
    }

    @Test
    public void testKeyDeserializerInstance_nullDeserDef_returnNull() throws Exception {
        assertNull(context.keyDeserializerInstance(null, null));
    }

    @Test
    public void testKeyDeserializerInstance_deserDefIsClass_returnNewInstance() throws Exception {
        Class<?> deserDef = ClassUtil.findClass("com.fasterxml.jackson.databind.deser.BeanDeserializer");
        assertNotNull(context.keyDeserializerInstance(null, deserDef));
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstance_deserDefIsNotClass_throwException() throws Exception {
        context.keyDeserializerInstance(null, new ObjectMapper());
    }

    @Test
    public void testCopy_forDefaultDeserializationContextImpl_returnNewDefaultDeserializationContextInstance() {
        DefaultDeserializationContext.Impl contextImpl = new DefaultDeserializationContext.Impl(objectMapper.getDeserializerFactory());
        
        DefaultDeserializationContext copy = contextImpl.copy();
        
        assertNotSame(contextImpl, copy);
        assertEquals(contextImpl.getClass(), copy.getClass());
    }

    @Test
    public void testCreateInstance_createInstanceWithDeserializationConfigAndJsonParserAndInjectableValues_returnNewInstance() {
        DefaultDeserializationContext.Impl contextImpl = new DefaultDeserializationContext.Impl(objectMapper.getDeserializerFactory());
        
        DefaultDeserializationContext newInstance = contextImpl.createInstance(DeserializationConfig.getDefaults(), objectMapper.createParser("{}"), null);
        
        assertNotNull(newInstance);
        assertNotSame(contextImpl, newInstance);
    }

    @Test
    public void testWith_withDeserializerFactory_returnNewDefaultDeserializationContextInstance() {
        DefaultDeserializationContext.Impl contextImpl = new DefaultDeserializationContext.Impl(objectMapper.getDeserializerFactory());
        
        DefaultDeserializationContext newInstance = contextImpl.with(objectMapper.getDeserializerFactory());
        
        assertNotNull(newInstance);
        assertNotSame(contextImpl, newInstance);
    }

    @Test
    public void testDefaultDeserializationContextImpl() {
        DefaultDeserializationContext.Impl contextImpl = new DefaultDeserializationContext.Impl(objectMapper.getDeserializerFactory());
        assertNotNull(contextImpl);
    }

}