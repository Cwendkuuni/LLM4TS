import org.junit.Test;
import static org.junit.Assert.*;

public class WritableObjectIdTest {

    // Test object creation and getter method
    @Test
    public void testConstructorAndGetGenerator() {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId objectId = new WritableObjectId(generator);
        assertEquals(generator, objectId.generator);
    }
    
    // Test setting and getting the id
    @Test
    public void testSetAndGetId() {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId objectId = new WritableObjectId(generator);
        
        assertNull(objectId.id);
        
        Object id = new Object();
        objectId.id = id;
        
        assertEquals(id, objectId.id);
    }
    
    // Test writeAsId method when id is null
    @Test
    public void testWriteAsIdWithNullId() throws IOException {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId objectId = new WritableObjectId(generator);
        
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        
        assertFalse(objectId.writeAsId(gen, provider, w));
    }
    
    // Test writeAsId method when idWritten is false and alwaysAsId is false
    @Test
    public void testWriteAsIdWithIdWrittenFalse() throws IOException {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId objectId = new WritableObjectId(generator);
        
        objectId.id = new Object();
        
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        w.alwaysAsId = false;
        
        assertFalse(objectId.writeAsId(gen, provider, w));
    }
    
    // Test writeAsId method when idWritten is true
    @Test
    public void testWriteAsIdWithIdWrittenTrue() throws IOException {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId objectId = new WritableObjectId(generator);
        
        objectId.id = new Object();
        objectId.idWritten = true;
        
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        
        assertTrue(objectId.writeAsId(gen, provider, w));
    }
    
    // Test writeAsId method when alwaysAsId is true and gen.canWriteObjectId() is true
    @Test
    public void testWriteAsIdWithAlwaysAsIdAndCanWriteObjectId() throws IOException {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId objectId = new WritableObjectId(generator);
        
        objectId.id = new Object();
        
        JsonGenerator gen = new JsonGenerator();
        gen.setCanWriteObjectId(true);
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        w.alwaysAsId = true;
        
        assertTrue(objectId.writeAsId(gen, provider, w));
    }
    
    // Test writeAsId method when alwaysAsId is true and gen.canWriteObjectId() is false
    @Test
    public void testWriteAsIdWithAlwaysAsIdAndCannotWriteObjectId() throws IOException {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId objectId = new WritableObjectId(generator);
        
        objectId.id = new Object();
        
        JsonGenerator gen = new JsonGenerator();
        gen.setCanWriteObjectId(false);
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        w.alwaysAsId = true;
        
        assertTrue(objectId.writeAsId(gen, provider, w));
    }
    
    // Test generateId method
    @Test
    public void testGenerateId() {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId objectId = new WritableObjectId(generator);
        
        Object forPojo = new Object();
        
        assertNull(objectId.generateId(forPojo));
        assertNotNull(objectId.id);
    }
    
    // Test writeAsField method when gen.canWriteObjectId() is true
    @Test
    public void testWriteAsFieldWithCanWriteObjectId() throws IOException {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId objectId = new WritableObjectId(generator);
        
        objectId.id = new Object();
        
        JsonGenerator gen = new JsonGenerator();
        gen.setCanWriteObjectId(true);
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        
        objectId.writeAsField(gen, provider, w);
        assertTrue(objectId.idWritten);
    }
    
    // Test writeAsField method when gen.canWriteObjectId() is false
    @Test
    public void testWriteAsFieldWithoutCanWriteObjectId() throws IOException {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId objectId = new WritableObjectId(generator);
        
        objectId.id = new Object();
        
        JsonGenerator gen = new JsonGenerator();
        gen.setCanWriteObjectId(false);
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        
        objectId.writeAsField(gen, provider, w);
        assertTrue(objectId.idWritten);
    }
}