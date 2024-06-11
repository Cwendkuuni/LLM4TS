import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedConstructor();
        creatorCollector.setDefaultCreator(creator);
        assertEquals(creator, creatorCollector.getDefaultCreator());
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedConstructor();
        creatorCollector.addStringCreator(creator, true);
        assertArrayEquals(new AnnotatedWithParams[]{null, creator, null, null, null, null, null, null, null}, creatorCollector.getCreators());
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedConstructor();
        creatorCollector.addIntCreator(creator, true);
        assertArrayEquals(new AnnotatedWithParams[]{null, null, creator, null, null, null, null, null, null}, creatorCollector.getCreators());
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedConstructor();
        creatorCollector.addLongCreator(creator, true);
        assertArrayEquals(new AnnotatedWithParams[]{null, null, null, creator, null, null, null, null, null}, creatorCollector.getCreators());
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedConstructor();
        creatorCollector.addDoubleCreator(creator, true);
        assertArrayEquals(new AnnotatedWithParams[]{null, null, null, null, creator, null, null, null, null}, creatorCollector.getCreators());
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedConstructor();
        creatorCollector.addBooleanCreator(creator, true);
        assertArrayEquals(new AnnotatedWithParams[]{null, null, null, null, null, creator, null, null, null}, creatorCollector.getCreators());
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedConstructor();
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        creatorCollector.addDelegatingCreator(creator, true, properties);
        assertArrayEquals(new AnnotatedWithParams[]{null, null, null, null, null, null, creator, null, null}, creatorCollector.getCreators());
        assertArrayEquals(properties, creatorCollector.getDelegateArgs());
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedWithParams creator = new AnnotatedConstructor();
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        creatorCollector.addPropertyCreator(creator, true, properties);
        assertArrayEquals(new AnnotatedWithParams[]{null, null, null, null, null, null, null, creator, null}, creatorCollector.getCreators());
        assertArrayEquals(properties, creatorCollector.getPropertyBasedArgs());
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector();
        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);
        assertEquals(parameter, creatorCollector.getIncompleteParameter());
    }
}