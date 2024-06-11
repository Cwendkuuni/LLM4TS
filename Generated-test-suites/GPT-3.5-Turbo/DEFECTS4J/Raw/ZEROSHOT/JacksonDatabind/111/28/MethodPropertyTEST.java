import static org.junit.Assert.*;

import org.junit.Test;

public class MethodPropertyTest {

    @Test
    public void testWithNullProvider() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        property.withNullProvider(null);
    }

    @Test
    public void testFixAccess() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        property.fixAccess(null);
    }

    @Test
    public void testGetAnnotation() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        property.getAnnotation(null);
    }

    @Test
    public void testGetMember() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        property.getMember();
    }

    @Test
    public void testDeserializeAndSet() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        property.deserializeAndSet(null, null, null);
    }

    @Test
    public void testDeserializeSetAndReturn() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        property.deserializeSetAndReturn(null, null, null);
    }

    @Test
    public void testSet() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        property.set(null, null);
    }

    @Test
    public void testSetAndReturn() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        property.setAndReturn(null, null);
    }

    @Test
    public void testWithName() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        property.withName(null);
    }

    @Test
    public void testWithValueDeserializer() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        property.withValueDeserializer(null);
    }

    @Test
    public void testConstructor1() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
    }

    @Test
    public void testConstructor2() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
    }

    @Test
    public void testConstructor3() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
    }

    @Test
    public void testConstructor4() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
    }

    @Test
    public void testConstructor5() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
    }

    @Test
    public void testConstructor6() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
    }

    @Test
    public void testConstructor7() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
    }

    @Test
    public void testConstructor8() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
    }

    @Test
    public void testConstructor9() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
    }
}