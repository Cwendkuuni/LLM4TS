import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DevirtualizePrototypeMethodsTest {

    private DevirtualizePrototypeMethods devirtualizePrototypeMethods;

    @Before
    public void setUp() {
        devirtualizePrototypeMethods = new DevirtualizePrototypeMethods();
    }

    @Test
    public void testRewriteDefinitionIfEligible_withEligibleDefinition() {
        // Arrange
        DefinitionSite definitionSite = new DefinitionSite();
        definitionSite.setInExterns(false);
        definitionSite.setInGlobalScope(true);
        definitionSite.setNode(new Node());
        definitionSite.setModule(null);
        Definition definition = new Definition();
        definitionSite.setDefinition(definition);
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
        defFinder.addUseSite(definitionSite, new UseSite());

        // Act
        devirtualizePrototypeMethods.rewriteDefinitionIfEligible(definitionSite, defFinder);

        // Assert
        // Add assertions here
    }

    @Test
    public void testRewriteDefinitionIfEligible_withNonEligibleDefinition() {
        // Arrange
        DefinitionSite definitionSite = new DefinitionSite();
        definitionSite.setInExterns(true);
        definitionSite.setInGlobalScope(false);
        definitionSite.setNode(new Node());
        definitionSite.setModule(null);
        Definition definition = new Definition();
        definitionSite.setDefinition(definition);
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
        defFinder.addUseSite(definitionSite, new UseSite());

        // Act
        devirtualizePrototypeMethods.rewriteDefinitionIfEligible(definitionSite, defFinder);

        // Assert
        // Add assertions here
    }

    @Test
    public void testIsEligibleDefinition_withEligibleDefinition() {
        // Arrange
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
        DefinitionSite definitionSite = new DefinitionSite();
        definitionSite.setDefinition(new Definition());
        definitionSite.setModule(null);

        // Act
        boolean result = devirtualizePrototypeMethods.isEligibleDefinition(defFinder, definitionSite);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEligibleDefinition_withNonEligibleDefinition() {
        // Arrange
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
        DefinitionSite definitionSite = new DefinitionSite();
        definitionSite.setDefinition(new Definition());
        definitionSite.setModule(null);
        definitionSite.getDefinition().setRValue(new Node());

        // Act
        boolean result = devirtualizePrototypeMethods.isEligibleDefinition(defFinder, definitionSite);

        // Assert
        assertFalse(result);
    }

    // Add more test cases for other methods

}