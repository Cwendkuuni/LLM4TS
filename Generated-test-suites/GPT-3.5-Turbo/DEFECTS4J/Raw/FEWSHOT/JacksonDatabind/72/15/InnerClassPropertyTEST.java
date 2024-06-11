import static org.junit.Assert.*;
import org.junit.Test;

public class InnerClassPropertyTest {
    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<?>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        assertNotNull(property);
        assertEquals(delegate, property._delegate);
        assertEquals(ctor, property._creator);
    }

    @Test
    public void testConstructorWithAnnotated() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        InnerClassProperty src = new InnerClassProperty(delegate, new AnnotatedConstructor());
        AnnotatedConstructor ann = new AnnotatedConstructor();
        InnerClassProperty property = new InnerClassProperty(src, ann);
        assertNotNull(property);
        assertEquals(src, property);
        assertEquals(delegate, property._delegate);
        assertEquals(ann, property._annotated);
    }

    @Test
    public void testConstructorWithDeserializer() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        InnerClassProperty src = new InnerClassProperty(delegate, new AnnotatedConstructor());
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty property = new InnerClassProperty(src, deser);
        assertNotNull(property);
        assertEquals(src, property);
        assertEquals(delegate, property._delegate);
        assertEquals(deser, property._delegate.getValueDeserializer());
    }

    @Test
    public void testConstructorWithNewName() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        InnerClassProperty src = new InnerClassProperty(delegate, new AnnotatedConstructor());
        PropertyName newName = new PropertyName("newName");
        InnerClassProperty property = new InnerClassProperty(src, newName);
        assertNotNull(property);
        assertEquals(src, property);
        assertEquals(delegate, property._delegate);
        assertEquals(newName, property._delegate.getFullName());
    }

    @Test
    public void testGetName() {
        InnerClassProperty property = new InnerClassProperty();
        assertNull(property.getFullName());
    }
    
    @Test
    public void testWithValueDeserializer() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        InnerClassProperty src = new InnerClassProperty(delegate, new AnnotatedConstructor());
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty property = src.withValueDeserializer(deser);
        assertNotNull(property);
        assertEquals(src, property);
        assertEquals(delegate, property._delegate);
        assertEquals(deser, property._delegate.getValueDeserializer());
    }

    @Test
    public void testAssignIndex() {
        InnerClassProperty property = new InnerClassProperty();
        int index = 0;
        property.assignIndex(index);
        assertEquals(index, property._delegate.getPropertyIndex());
    }

    @Test
    public void testGetAnnotation() {
        InnerClassProperty property = new InnerClassProperty();
        Class<A> acls = A.class;
        assertNull(property.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        InnerClassProperty property = new InnerClassProperty();
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        InnerClassProperty property = new InnerClassProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        property.deserializeAndSet(jp, ctxt, bean);
        // TODO: Add assertions
    }

    @Test
    public void testDeserializeSetAndReturn() {
        InnerClassProperty property = new InnerClassProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(jp, ctxt, instance);
        // TODO: Add assertions
    }

    @Test
    public void testSet() {
        InnerClassProperty property = new InnerClassProperty();
        Object instance = new Object();
        Object value = new Object();
        property.set(instance, value);
        // TODO: Add assertions
    }

    @Test
    public void testSetAndReturn() {
        InnerClassProperty property = new InnerClassProperty();
        Object instance = new Object();
        Object value = new Object();
        Object result = property.setAndReturn(instance, value);
        // TODO: Add assertions
    }
}