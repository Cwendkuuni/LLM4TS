import org.junit.Test;
import static org.junit.Assert.*;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor1() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<?>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        assertEquals(delegate, property._delegate);
        assertEquals(ctor, property._creator);
    }

    @Test
    public void testConstructor2() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<?>();
        AnnotatedConstructor ann = new AnnotatedConstructor(null, ctor, null, null);
        InnerClassProperty property = new InnerClassProperty(delegate, ann);
        assertEquals(delegate, property._delegate);
        assertEquals(ann, property._annotated);
        assertEquals(ctor, property._creator);
    }

    @Test
    public void testConstructor3() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<?>();
        JsonDeserializer<?> deser = new JsonDeserializer<?>();
        InnerClassProperty src = new InnerClassProperty(delegate, ctor);
        InnerClassProperty property = new InnerClassProperty(src, deser);
        assertEquals(delegate.withValueDeserializer(deser), property._delegate);
        assertEquals(ctor, property._creator);
    }

    @Test
    public void testConstructor4() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<?>();
        PropertyName newName = new PropertyName();
        InnerClassProperty src = new InnerClassProperty(delegate, ctor);
        InnerClassProperty property = new InnerClassProperty(src, newName);
        assertEquals(delegate.withName(newName), property._delegate);
        assertEquals(ctor, property._creator);
    }

    @Test
    public void testWithIndex() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<?>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        property.assignIndex(1);
        assertEquals(1, property.getPropertyIndex());
    }

    @Test
    public void testGetAnnotation() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<?>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        assertNull(property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<?>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        SettableBeanProperty delegate = new SettableBeanProperty();
        delegate.set(bean, "value"); // dummy value
        Constructor<?> ctor = new Constructor<?>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        try {
            property.deserializeAndSet(jp, ctxt, bean);
            assertEquals("value", delegate.get(bean)); // check if value is correctly set
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        SettableBeanProperty delegate = new SettableBeanProperty();
        delegate.set(instance, "value"); // dummy value
        Constructor<?> ctor = new Constructor<?>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        try {
            Object result = property.deserializeSetAndReturn(jp, ctxt, instance);
            assertEquals("value", delegate.get(instance)); // check if value is correctly set
            assertEquals(instance, result); // check if instance is returned
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        SettableBeanProperty delegate = new SettableBeanProperty();
        delegate.set(instance, "value"); // dummy value
        Constructor<?> ctor = new Constructor<?>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        try {
            Object result = property.setAndReturn(instance, "new value");
            assertEquals("new value", delegate.get(instance)); // check if value is correctly set
            assertEquals(instance, result); // check if instance is returned
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadResolve() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<?>();
        AnnotatedConstructor ann = new AnnotatedConstructor(null, ctor, null, null);
        InnerClassProperty src = new InnerClassProperty(delegate, ann);
        InnerClassProperty property = new InnerClassProperty(src, ann);
        assertEquals(property, property.readResolve());
    }

    @Test
    public void testWriteReplace() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<?>();
        AnnotatedConstructor ann = new AnnotatedConstructor(null, ctor, null, null);
        InnerClassProperty src = new InnerClassProperty(delegate, ann);
        InnerClassProperty property = new InnerClassProperty(src, ann);
        assertEquals(property, property.writeReplace());
    }

}