import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testConstructValueInstantiator() {
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector.setDefaultCreator(null);
        creatorCollector.addStringCreator(null, false);
        creatorCollector.addIntCreator(null, false);
        creatorCollector.addLongCreator(null, false);
        creatorCollector.addDoubleCreator(null, false);
        creatorCollector.addBooleanCreator(null, false);
        creatorCollector.addDelegatingCreator(null, false, null);
        creatorCollector.addPropertyCreator(null, false, null);
        creatorCollector.addIncompeteParameter(null);
        creatorCollector.hasDefaultCreator();
        creatorCollector.hasDelegatingCreator();
        creatorCollector.hasPropertyBasedCreator();
    }
}