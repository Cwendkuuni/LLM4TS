import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectIdValuePropertyTest {
    
    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testConstructorWithDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty propertyWithDeserializer = property.withValueDeserializer(deser);
        
        assertEquals(objectIdReader.propertyName, propertyWithDeserializer.getName());
        assertEquals(objectIdReader.getIdType(), propertyWithDeserializer.getType());
        assertEquals(metadata, propertyWithDeserializer.getMetadata());
        assertEquals(deser, propertyWithDeserializer.getValueDeserializer());
        assertEquals(objectIdReader, propertyWithDeserializer._objectIdReader);
    }
    
    @Test
    public void testConstructorWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty propertyWithName = property.withName(newName);
        
        assertEquals(newName, propertyWithName.getName());
        assertEquals(objectIdReader.getIdType(), propertyWithName.getType());
        assertEquals(metadata, propertyWithName.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), propertyWithName.getValueDeserializer());
        assertEquals(objectIdReader, propertyWithName._objectIdReader);
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getAnnotation(null));
    }
    
    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        //implement this test case
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        //implement this test case
    }

    @Test
    public void testSet() throws IOException {
        //implement this test case
    }

    @Test
    public void testSetAndReturn() throws IOException {
        //implement this test case
    }
}