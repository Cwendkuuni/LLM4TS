
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.Annotation;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testConstructor() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Assert.assertEquals(objectIdReferenceProperty._forward, forward);
        Assert.assertEquals(objectIdReferenceProperty._objectIdInfo, objectIdInfo);
    }

    @Test
    public void testCopyConstructor() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(src, deser, nva);
        Assert.assertEquals(objectIdReferenceProperty._forward, src._forward);
        Assert.assertEquals(objectIdReferenceProperty._objectIdInfo, src._objectIdInfo);
    }

    @Test
    public void testCopyConstructorWithPropertyName() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        PropertyName newName = new PropertyName();
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(src, newName);
        Assert.assertEquals(objectIdReferenceProperty._forward, src._forward);
        Assert.assertEquals(objectIdReferenceProperty._objectIdInfo, src._objectIdInfo);
        Assert.assertEquals(objectIdReferenceProperty._propertyName, newName);
    }

    @Test
    public void testWithName() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        PropertyName newName = new PropertyName();
        ObjectIdReferenceProperty objectIdReferenceProperty = src.withName(newName);
        Assert.assertEquals(objectIdReferenceProperty._forward, src._forward);
        Assert.assertEquals(objectIdReferenceProperty._objectIdInfo, src._objectIdInfo);
        Assert.assertEquals(objectIdReferenceProperty._propertyName, newName);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdReferenceProperty objectIdReferenceProperty = src.withValueDeserializer(deser);
        Assert.assertEquals(objectIdReferenceProperty._forward, src._forward);
        Assert.assertEquals(objectIdReferenceProperty._objectIdInfo, src._objectIdInfo);
        Assert.assertEquals(objectIdReferenceProperty._valueDeserializer, deser);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty objectIdReferenceProperty = src.withNullProvider(nva);
        Assert.assertEquals(objectIdReferenceProperty._forward, src._forward);
        Assert.assertEquals(objectIdReferenceProperty._objectIdInfo, src._objectIdInfo);
        Assert.assertEquals(objectIdReferenceProperty._nullProvider, nva);
    }

    @Test
    public void testFixAccess() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        src._forward = new SettableBeanProperty();
        DeserializationConfig config = new DeserializationConfig();
        src.fixAccess(config);
        Assert.assertNotNull(src._forward);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        src._forward = new SettableBeanProperty();
        Annotation acls = new Annotation();
        Assert.assertEquals(src.getAnnotation(acls), src._forward.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        src._forward = new SettableBeanProperty();
        Assert.assertEquals(src.getMember(), src._forward.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        src._forward = new SettableBeanProperty();
        Assert.assertEquals(src.getCreatorIndex(), src._forward.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        src._forward = new SettableBeanProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            src.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        src._forward = new SettableBeanProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            src.deserializeSetAndReturn(p, ctxt, instance);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testSet() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        src._forward = new SettableBeanProperty();
        Object instance = new Object();
        try {
            src.set(instance, new Object());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        src._forward = new SettableBeanProperty();
        Object instance = new Object();
        try {
            src.setAndReturn(instance, new Object());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testPropertyReferringHandleResolvedForwardReference() {
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(
                new ObjectIdReferenceProperty(), new UnresolvedForwardReference(), Object.class, new Object());

        try {
            propertyReferring.handleResolvedForwardReference(new Object(), new Object());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (IllegalArgumentException e) {
            Assert.fail(e.getMessage());
        }
    }
}    
