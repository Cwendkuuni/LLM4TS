import org.junit.Assert;
import org.junit.Test;

public class SettableAnyPropertyTest {

    @Test
    public void testDeserializeAndSet() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);

        try {
            property.deserializeAndSet(null, null, null, "");
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        } catch (Exception e) {
            Assert.fail("Unexpected exception " + e.toString());
        }
    }

    @Test
    public void testDeserialize() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);

        try {
            property.deserialize(null, null);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        } catch (Exception e) {
            Assert.fail("Unexpected exception " + e.toString());
        }
    }

    @Test
    public void testSet() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);

        try {
            property.set(null, null, null);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        } catch (Exception e) {
            Assert.fail("Unexpected exception " + e.toString());
        }
    }

    @Test
    public void testThrowAsIOE() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);

        try {
            property._throwAsIOE(null, null, null);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        } catch (Exception e) {
            Assert.fail("Unexpected exception " + e.toString());
        }
    }

}