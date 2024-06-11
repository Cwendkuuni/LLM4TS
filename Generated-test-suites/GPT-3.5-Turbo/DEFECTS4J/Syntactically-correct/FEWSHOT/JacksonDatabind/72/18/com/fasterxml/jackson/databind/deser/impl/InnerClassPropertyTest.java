import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.deser.impl.*;

public class InnerClassPropertyTest {
    
    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        
        assertSame(delegate, property._delegate);
        assertSame(ctor, property._creator);
    }
    
    @Test
    public void testSerializationConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty src = new InnerClassProperty(delegate, ctor);
        AnnotatedConstructor ann = new AnnotatedConstructor(null, ctor, null, null);
        InnerClassProperty property = new InnerClassProperty(src, ann);
        
        assertSame(delegate, property._delegate);
        assertSame(ann, property._annotated);
        assertSame(ctor, property._creator);
    }
    
    @Test
    public void testDeserializationConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty src = new InnerClassProperty(delegate, ctor);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty property = new InnerClassProperty(src, deser);
        
        assertSame(delegate, property._delegate);
        assertSame(deser, property._delegate.getValueDeserializer());
        assertSame(ctor, property._creator);
    }
    
    @Test
    public void testPropertyNameConstructor() {
        PropertyName newName = new PropertyName("test");
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty src = new InnerClassProperty(delegate, ctor);
        InnerClassProperty property = new InnerClassProperty(src, newName);
        
        assertEquals(newName, property._delegate.getName());
        assertSame(delegate, property._delegate.withName(newName));
        assertSame(ctor, property._creator);
    }
    
    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("test");
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty src = new InnerClassProperty(delegate, ctor);
        InnerClassProperty property = src.withName(newName);
        
        assertEquals(newName, property._delegate.getName());
        assertSame(delegate, property._delegate.withName(newName));
        assertSame(ctor, property._creator);
    }
    
    @Test
    public void testWithValueDeserializer() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty src = new InnerClassProperty(delegate, ctor);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty property = src.withValueDeserializer(deser);
        
        assertSame(delegate, property._delegate);
        assertSame(deser, property._delegate.getValueDeserializer());
        assertSame(ctor, property._creator);
    }
    
    @Test
    public void testAssignIndex() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        
        property.assignIndex(1);
        
        assertEquals(1, delegate.getPropertyIndex());
    }
    
    @Test
    public void testGetPropertyIndex() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        
        delegate.assignIndex(2);
        
        assertEquals(2, property.getPropertyIndex());
    }
    
    @Test
    public void testGetAnnotation() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        
        assertNull(property.getAnnotation(null));
    }
    
    @Test
    public void testGetMember() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        
        assertNull(property.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() {
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        InnerClassProperty property = new InnerClassProperty(new SettableBeanProperty(), new Constructor<>());
        Object bean = new Object();
        
        assertThrows(IOException.class, () -> property.deserializeAndSet(jp, ctxt, bean));
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        InnerClassProperty property = new InnerClassProperty(new SettableBeanProperty(), new Constructor<>());
        Object instance = new Object();
        
        assertThrows(IOException.class, () -> property.deserializeSetAndReturn(jp, ctxt, instance));
    }
    
    @Test
    public void testSet() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        Object instance = new Object();
        
        assertThrows(IOException.class, () -> property.set(instance, null));
    }
    
    @Test
    public void testSetAndReturn() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        Object instance = new Object();
        
        assertThrows(IOException.class, () -> property.setAndReturn(instance, null));
    }
    
    @Test
    public void testReadResolve() {
        InnerClassProperty property = new InnerClassProperty(new SettableBeanProperty(), new Constructor<>());
        
        assertEquals(property, property.readResolve());
    }
    
    @Test
    public void testWriteReplace() {
        InnerClassProperty property = new InnerClassProperty(new SettableBeanProperty(), new Constructor<>());
        
        assertEquals(property, property.writeReplace());
    }
}