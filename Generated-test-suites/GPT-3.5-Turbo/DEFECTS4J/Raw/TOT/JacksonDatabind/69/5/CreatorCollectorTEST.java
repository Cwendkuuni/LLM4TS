import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        creatorCollector = new CreatorCollector(null, null);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            // Implement required methods
        };
        creatorCollector.setDefaultCreator(creator);

        assertEquals(creator, creatorCollector._creators[0]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            // Implement required methods
        };
        boolean explicit = true;
        creatorCollector.addStringCreator(creator, explicit);

        assertEquals(creator, creatorCollector._creators[1]);
        assertEquals(explicit ? 1 : 0, creatorCollector._explicitCreators & (1 << 1));
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            // Implement required methods
        };
        boolean explicit = true;
        creatorCollector.addIntCreator(creator, explicit);

        assertEquals(creator, creatorCollector._creators[2]);
        assertEquals(explicit ? 1 : 0, creatorCollector._explicitCreators & (1 << 2));
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            // Implement required methods
        };
        boolean explicit = true;
        creatorCollector.addLongCreator(creator, explicit);

        assertEquals(creator, creatorCollector._creators[3]);
        assertEquals(explicit ? 1 : 0, creatorCollector._explicitCreators & (1 << 3));
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            // Implement required methods
        };
        boolean explicit = true;
        creatorCollector.addDoubleCreator(creator, explicit);

        assertEquals(creator, creatorCollector._creators[4]);
        assertEquals(explicit ? 1 : 0, creatorCollector._explicitCreators & (1 << 4));
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            // Implement required methods
        };
        boolean explicit = true;
        creatorCollector.addBooleanCreator(creator, explicit);

        assertEquals(creator, creatorCollector._creators[5]);
        assertEquals(explicit ? 1 : 0, creatorCollector._explicitCreators & (1 << 5));
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            // Implement required methods
        };
        boolean explicit = true;
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        creatorCollector.addDelegatingCreator(creator, explicit, injectables);

        assertEquals(creator, creatorCollector._creators[6]);
        assertEquals(explicit ? 1 : 0, creatorCollector._explicitCreators & (1 << 6));
        assertArrayEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            // Implement required methods
        };
        boolean explicit = true;
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        creatorCollector.addPropertyCreator(creator, explicit, properties);

        assertEquals(creator, creatorCollector._creators[7]);
        assertEquals(explicit ? 1 : 0, creatorCollector._explicitCreators & (1 << 7));
        assertArrayEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = new AnnotatedParameter(null, null, null);
        creatorCollector.addIncompeteParameter(parameter);

        assertEquals(parameter, creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        creatorCollector._creators[0] = new AnnotatedWithParams() {
        };
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testHasDelegatingCreator() {
        creatorCollector._creators[6] = new AnnotatedWithParams() {
        };
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testHasPropertyBasedCreator() {
        creatorCollector._creators[7] = new AnnotatedWithParams() {
        };
        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }
}