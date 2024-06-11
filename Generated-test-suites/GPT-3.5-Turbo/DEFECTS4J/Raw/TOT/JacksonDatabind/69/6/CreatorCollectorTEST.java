import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        creatorCollector = new CreatorCollector(null, null);
    }

    @Test
    public void testSetDefaultCreator() {
        // Typical use case
        creatorCollector.setDefaultCreator(null);

        // Edge case: null creator
        creatorCollector.setDefaultCreator(null);

        assertNotNull(creatorCollector._creators[0]);
    }

    @Test
    public void testAddStringCreator() {
        // Typical use case
        creatorCollector.addStringCreator(null, true);

        // Edge case: null creator
        creatorCollector.addStringCreator(null, true);

        assertNotNull(creatorCollector._creators[1]);
    }

    @Test
    public void testAddIntCreator() {
        // Typical use case
        creatorCollector.addIntCreator(null, true);

        // Edge case: null creator
        creatorCollector.addIntCreator(null, true);

        assertNotNull(creatorCollector._creators[2]);
    }

    @Test
    public void testAddLongCreator() {
        // Typical use case
        creatorCollector.addLongCreator(null, true);

        // Edge case: null creator
        creatorCollector.addLongCreator(null, true);

        assertNotNull(creatorCollector._creators[3]);
    }

    @Test
    public void testAddDoubleCreator() {
        // Typical use case
        creatorCollector.addDoubleCreator(null, true);

        // Edge case: null creator
        creatorCollector.addDoubleCreator(null, true);

        assertNotNull(creatorCollector._creators[4]);
    }

    @Test
    public void testAddBooleanCreator() {
        // Typical use case
        creatorCollector.addBooleanCreator(null, true);

        // Edge case: null creator
        creatorCollector.addBooleanCreator(null, true);

        assertNotNull(creatorCollector._creators[5]);
    }

    @Test
    public void testAddDelegatingCreator() {
        // Typical use case
        creatorCollector.addDelegatingCreator(null, true, null);

        // Edge case: null creator
        creatorCollector.addDelegatingCreator(null, true, null);

        assertNotNull(creatorCollector._creators[6]);
    }

    @Test
    public void testAddPropertyCreator() {
        // Typical use case
        creatorCollector.addPropertyCreator(null, true, null);

        // Edge case: null creator
        creatorCollector.addPropertyCreator(null, true, null);

        assertNotNull(creatorCollector._creators[7]);
    }

    @Test
    public void testAddIncompeteParameter() {
        // Typical use case
        creatorCollector.addIncompeteParameter(null);

        // Edge case: null parameter
        creatorCollector.addIncompeteParameter(null);

        assertNotNull(creatorCollector._incompleteParameter);
    }
}