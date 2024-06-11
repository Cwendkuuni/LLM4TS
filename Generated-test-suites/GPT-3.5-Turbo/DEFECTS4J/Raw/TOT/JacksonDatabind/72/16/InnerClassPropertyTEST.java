import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class InnerClassPropertyTest {
    
    private InnerClassProperty innerClass;

    @Before
    public void setUp() {
        innerClass = new InnerClassProperty(new SettableBeanProperty(), null);
    }

    @Test
    public void testDeserializeAndSet_ValueNull() throws Exception {
        // Arrange
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        jp.setCurrentToken(JsonToken.VALUE_NULL);
        
        // Act
        innerClass.deserializeAndSet(jp, ctxt, bean);
        
        // Assert
        assertEquals(null, innerClass.get(bean));
    }
    
    @Test
    public void testDeserializeAndSet_ValueTypeDeserializer() throws Exception {
        // Arrange
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        JsonDeserializer<?> valueTypeDeserializer = new JsonDeserializer<>();
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        
        // Act
        innerClass.deserializeAndSet(jp, ctxt, bean);
        
        // Assert
        assertEquals(bean, innerClass.get(bean));
    }
    
    @Test
    public void testDeserializeAndSet_NormalCase() throws Exception {
        // Arrange
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        
        // Act
        innerClass.deserializeAndSet(jp, ctxt, bean);
        
        // Assert
        assertEquals(bean, innerClass.get(bean));
    }
    
    @Test
    public void testDeserializeSetAndReturn_Success() throws Exception {
        // Arrange
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        
        // Act
        Object result = innerClass.deserializeSetAndReturn(jp, ctxt, instance);
        
        // Assert
        assertEquals(instance, result);
        assertEquals(instance, innerClass.get(instance));
    }
    
    @Test
    public void testSet_NormalCase() throws Exception {
        // Arrange
        Object instance = new Object();
        Object value = new Object();
        
        // Act
        innerClass.set(instance, value);
        
        // Assert
        assertEquals(value, innerClass.get(instance));
    }
    
    @Test
    public void testSetAndReturn_NormalCase() throws Exception {
        // Arrange
        Object instance = new Object();
        Object value = new Object();
        
        // Act
        Object result = innerClass.setAndReturn(instance, value);
        
        // Assert
        assertEquals(value, innerClass.get(instance));
        assertEquals(instance, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInnerClassProperty_Constructor_WithNullConstructor() {
        // Arrange
        SettableBeanProperty delegate = new SettableBeanProperty();
        
        // Act
        new InnerClassProperty(delegate, null);
    }
    
    @Test
    public void testInnerClassProperty_WithAnnotatedConstructor() {
        // Arrange
        InnerClassProperty src = new InnerClassProperty(new SettableBeanProperty(), null);
        AnnotatedConstructor ann = new AnnotatedConstructor(null, null, null, null);
        
        // Act
        InnerClassProperty result = new InnerClassProperty(src, ann);
        
        // Assert
        assertEquals(src.getDelegate(), result.getDelegate());
        assertEquals(ann.getAnnotated(), result.getCreator());
    }
    
    @Test
    public void testInnerClassProperty_WithJsonDeserializer() {
        // Arrange
        InnerClassProperty src = new InnerClassProperty(new SettableBeanProperty(), null);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        
        // Act
        InnerClassProperty result = new InnerClassProperty(src, deser);
        
        // Assert
        assertEquals(deser, result.getValueDeserializer());
    }
    
    @Test
    public void testInnerClassProperty_WithPropertyName() {
        // Arrange
        InnerClassProperty src = new InnerClassProperty(new SettableBeanProperty(), null);
        PropertyName newName = new PropertyName("newName");
        
        // Act
        InnerClassProperty result = new InnerClassProperty(src, newName);
        
        // Assert
        assertEquals(newName, result.getFullName());
    }
    
    @Test
    public void testInnerClassProperty_WithName() {
        // Arrange
        InnerClassProperty src = new InnerClassProperty(new SettableBeanProperty(), null);
        PropertyName newName = new PropertyName("newName");
        
        // Act
        InnerClassProperty result = src.withName(newName);
        
        // Assert
        assertEquals(newName, result.getFullName());
    }
    
    @Test
    public void testInnerClassProperty_WithValueDeserializer() {
        // Arrange
        InnerClassProperty src = new InnerClassProperty(new SettableBeanProperty(), null);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        
        // Act
        InnerClassProperty result = src.withValueDeserializer(deser);
        
        // Assert
        assertEquals(deser, result.getValueDeserializer());
    }
    
    @Test
    public void testAssignIndex() {
        // Arrange
        int index = 5;
        
        // Act
        innerClass.assignIndex(index);
        
        // Assert
        assertEquals(index, innerClass.getPropertyIndex());
    }
    
    @Test
    public void testGetPropertyIndex() {
        // Arrange
        int index = 10;
        innerClass.assignIndex(index);
        
        // Act
        int result = innerClass.getPropertyIndex();
        
        // Assert
        assertEquals(index, result);
    }
    
    @Test
    public void testGetAnnotation() {
        // Arrange
        Annotation annotation = new Annotation() {
            // Annotation implementation
        };
        innerClass.addAnnotation(annotation);
        Class<Annotation> acls = Annotation.class;
        
        // Act
        Annotation result = innerClass.getAnnotation(acls);
        
        // Assert
        assertEquals(annotation, result);
    }
    
    @Test
    public void testGetMember() {
        // Arrange
        AnnotatedMember member = new AnnotatedMember() {
            // AnnotatedMember implementation
        };
        innerClass.setMember(member);
        
        // Act
        AnnotatedMember result = innerClass.getMember();
        
        // Assert
        assertEquals(member, result);
    }
    
    @Test
    public void testReadResolve_WithAnnotated() {
        // Arrange
        AnnotatedConstructor ann = new AnnotatedConstructor(null, null, null, null);
        InnerClassProperty src = new InnerClassProperty(new SettableBeanProperty(), null);
        src.setAnnotated(ann);
        
        // Act
        Object result = src.readResolve();
        
        // Assert
        assertEquals(ann, ((InnerClassProperty) result).getAnnotated());
    }
    
    @Test
    public void testReadResolve_WithoutAnnotated() {
        // Arrange
        InnerClassProperty src = new InnerClassProperty(new SettableBeanProperty(), null);
        
        // Act
        Object result = src.readResolve();
        
        // Assert
        assertEquals(src, result);
    }
    
    @Test
    public void testWriteReplace_WithAnnotated() {
        // Arrange
        AnnotatedConstructor ann = new AnnotatedConstructor(null, null, null, null);
        InnerClassProperty src = new InnerClassProperty(new SettableBeanProperty(), null);
        src.setAnnotated(ann);
        
        // Act
        Object result = src.writeReplace();
        
        // Assert
        assertEquals(src, result);
    }
    
    @Test
    public void testWriteReplace_WithoutAnnotated() {
        // Arrange
        InnerClassProperty src = new InnerClassProperty(new SettableBeanProperty(), null);
        
        // Act
        Object result = src.writeReplace();
        
        // Assert
        assertEquals(src, result);
    }
}