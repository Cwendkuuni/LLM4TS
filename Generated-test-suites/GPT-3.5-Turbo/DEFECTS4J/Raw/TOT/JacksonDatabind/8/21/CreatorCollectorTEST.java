import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.setDefaultCreator(null);
        assertEquals(null, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addStringCreator(null, false);
        assertEquals(null, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addIntCreator(null, false);
        assertEquals(null, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addLongCreator(null, false);
        assertEquals(null, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addDoubleCreator(null, false);
        assertEquals(null, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addBooleanCreator(null, false);
        assertEquals(null, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addDelegatingCreator(null, false, null);
        assertEquals(null, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addPropertyCreator(null, false, null);
        assertEquals(null, creatorCollector._creators[CreatorCollector.C_PROPS]);
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addIncompeteParameter(null);
        assertEquals(null, creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.setDefaultCreator(null);
        assertFalse(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testVerifyNonDupWithExplicitTrue() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams retValue = creatorCollector.verifyNonDup(null, CreatorCollector.C_DEFAULT, true);
        assertEquals(null, retValue);
        assertTrue(creatorCollector._creators[CreatorCollector.C_DEFAULT] != null);
    }

    @Test
    public void testVerifyNonDupWithExplicitFalse() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams retValue = creatorCollector.verifyNonDup(null, CreatorCollector.C_DEFAULT, false);
        assertEquals(null, retValue);
        assertTrue(creatorCollector._creators[CreatorCollector.C_DEFAULT] != null);
    }
}