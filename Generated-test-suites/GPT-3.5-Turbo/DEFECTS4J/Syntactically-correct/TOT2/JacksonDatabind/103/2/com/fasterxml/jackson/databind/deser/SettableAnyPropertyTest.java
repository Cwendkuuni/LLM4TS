import static org.junit.Assert.*;
import org.junit.Test;

public class SettableAnyPropertyTest {

    @Test
    public void testConstructorWithKeyDeserializer() {
        BeanProperty property = new BeanProperty();
        AnnotatedMember setter = new AnnotatedMethod();
        JavaType type = new JavaType();
        KeyDeserializer keyDeser = new KeyDeserializer();
        JsonDeserializer<Object> valueDeser = new JsonDeserializer<>();
        TypeDeserializer typeDeser = new TypeDeserializer();
        SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        
        assertNotNull(sap);
        assertEquals(property, sap.getProperty());
        assertFalse(sap.hasValueDeserializer());
        assertEquals(type, sap.getType());
    }

    @Test
    public void testConstructorWithoutKeyDeserializer() {
        BeanProperty property = new BeanProperty();
        AnnotatedMember setter = new AnnotatedMethod();
        JavaType type = new JavaType();
        JsonDeserializer<Object> valueDeser = new JsonDeserializer<>();
        TypeDeserializer typeDeser = new TypeDeserializer();
        SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, valueDeser, typeDeser);
        
        assertNotNull(sap);
        assertEquals(property, sap.getProperty());
        assertFalse(sap.hasValueDeserializer());
        assertEquals(type, sap.getType());
    }

    @Test
    public void testWithValueDeserializer() {
        BeanProperty property = new BeanProperty();
        AnnotatedMember setter = new AnnotatedMethod();
        JavaType type = new JavaType();
        JsonDeserializer<Object> valueDeser = new JsonDeserializer<>();
        TypeDeserializer typeDeser = new TypeDeserializer();
        SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, valueDeser, typeDeser);
        
        JsonDeserializer<Object> newDeser = new JsonDeserializer<>();
        SettableAnyProperty newSap = sap.withValueDeserializer(newDeser);
        
        assertNotNull(newSap);
        assertEquals(property, newSap.getProperty());
        assertFalse(newSap.hasValueDeserializer());
        assertEquals(type, newSap.getType());
    }

    // Add more test cases for all other methods

}