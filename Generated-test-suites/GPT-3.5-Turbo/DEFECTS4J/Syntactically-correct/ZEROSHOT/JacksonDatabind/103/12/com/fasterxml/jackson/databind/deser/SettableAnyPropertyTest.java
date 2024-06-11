import org.junit.Test;

public class SettableAnyPropertyTest {

    @Test
    public void testDeserializeAndSet() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        property.deserializeAndSet(null, null, null, null);
    }

    @Test
    public void testDeserialize() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        property.deserialize(null, null);
    }

    @Test
    public void testSet() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        property.set(null, null, null);
    }

    @Test
    public void testThrowAsIOE() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        try {
            property._throwAsIOE(null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToString() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        property.toString();
    }

    @Test
    public void testDecodePropagated() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        property.decodePropagated(null, null);
    }

    @Test
    public void testDeserializeAnySetter() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        property.deserializeAnySetter(null, null, null);
    }

    @Test
    public void testFindInjectableValue() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        property.findInjectableValue(null, null, null);
    }

    @Test
    public void testFixAccess() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        property.fixAccess(null);
    }

    @Test
    public void testKeyFromHandle() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        property.keyFromHandle(null, null);
    }

    @Test
    public void testReadResolve() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        property.readResolve();
    }

    @Test
    public void testSetValueFromHandle() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        property.setValueFromHandle(null, null);
    }

    @Test
    public void testWithIgnorals() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        property.withIgnorals(null);
    }
}