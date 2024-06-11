package jigl.image.ops;

import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.SimpleOperator;
import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.ops.ConnectedComponents;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConnectedComponentsTest {
    @Test
    public void testConstructor() {
        // Test for four-connected neighbors and background value of 0
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertEquals(0, cc.background);
            assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.neighbors);
        } catch (InvalidArgumentException e) {
            fail("Exception not expected.");
        }

        // Test for four-connected neighbors and background value of 1
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
            assertEquals(1, cc.background);
            assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.neighbors);
        } catch (InvalidArgumentException e) {
            fail("Exception not expected.");
        }

        // Test for eight-connected neighbors and background value of 0
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
            assertEquals(0, cc.background);
            assertEquals(ConnectedComponents.EIGHT_CONNECTED, cc.neighbors);
        } catch (InvalidArgumentException e) {
            fail("Exception not expected.");
        }

        // Test for eight-connected neighbors and background value of 1
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            assertEquals(1, cc.background);
            assertEquals(ConnectedComponents.EIGHT_CONNECTED, cc.neighbors);
        } catch (InvalidArgumentException e) {
            fail("Exception not expected.");
        }

        // Test for invalid neighbors value and valid background value
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 0);
            fail("InvalidArgumentException expected.");
        } catch (InvalidArgumentException e) {
            // Exception expected
        }

        // Test for valid neighbors value and invalid background value
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
            fail("InvalidArgumentException expected.");
        } catch (InvalidArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testApply() {
        ConnectedComponents cc = null;
        BinaryImage input = null;
        GrayImage output = null;

        // Test for four-connected neighbors and background value of 0
        try {
            cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            input = new BinaryImage(4, 4);
            input.set(0, 0, 1);
            input.set(0, 1, 1);
            input.set(1, 0, 1);
            input.set(1, 1, 1);
            output = cc.apply(input);
            assertEquals(0, output.get(0, 0));
            assertEquals(0, output.get(0, 1));
            assertEquals(1, output.get(1, 0));
            assertEquals(1, output.get(1, 1));
        } catch (InvalidArgumentException e) {
            fail("Exception not expected.");
        }

        // Test for four-connected neighbors and background value of 1
        try {
            cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
            input = new BinaryImage(4, 4);
            input.set(0, 0, 0);
            input.set(0, 1, 0);
            input.set(1, 0, 0);
            input.set(1, 1, 0);
            output = cc.apply(input);
            assertEquals(1, output.get(0, 0));
            assertEquals(1, output.get(0, 1));
            assertEquals(0, output.get(1, 0));
            assertEquals(0, output.get(1, 1));
        } catch (InvalidArgumentException e) {
            fail("Exception not expected.");
        }

        // Test for eight-connected neighbors and background value of 0
        try {
            cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
            input = new BinaryImage(4, 4);
            input.set(0, 0, 1);
            input.set(0, 1, 1);
            input.set(1, 0, 1);
            input.set(1, 1, 1);
            output = cc.apply(input);
            assertEquals(0, output.get(0, 0));
            assertEquals(0, output.get(0, 1));
            assertEquals(0, output.get(1, 0));
            assertEquals(0, output.get(1, 1));
        } catch (InvalidArgumentException e) {
            fail("Exception not expected.");
        }

        // Test for eight-connected neighbors and background value of 1
        try {
            cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            input = new BinaryImage(4, 4);
            input.set(0, 0, 0);
            input.set(0, 1, 0);
            input.set(1, 0, 0);
            input.set(1, 1, 0);
            output = cc.apply(input);
            assertEquals(1, output.get(0, 0));
            assertEquals(1, output.get(0, 1));
            assertEquals(1, output.get(1, 0));
            assertEquals(1, output.get(1, 1));
        } catch (InvalidArgumentException e) {
            fail("Exception not expected.");
        }
    }
}