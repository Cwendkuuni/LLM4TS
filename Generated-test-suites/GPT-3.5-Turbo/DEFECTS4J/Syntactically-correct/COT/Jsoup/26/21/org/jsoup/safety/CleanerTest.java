
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        // Test clean method with a valid document
        // Expected outcome: the cleaned document should only contain elements allowed by the whitelist
    }

    @Test
    public void testIsValid() {
        // Test isValid method with a valid document
        // Expected outcome: isValid should return true since there are no tags or attributes that need to be removed

        // Test isValid method with an invalid document
        // Expected outcome: isValid should return false since there are tags or attributes that need to be removed
    }

    @Test
    public void testCopySafeNodes() {
        // Test copySafeNodes method with a valid source element
        // Expected outcome: the destination element should only contain trusted nodes (tags, attributes, text)

        // Test copySafeNodes method with a source element containing unsafe tags
        // Expected outcome: the destination element should not contain any unsafe tags

        // Test copySafeNodes method with a source element containing unsafe attributes
        // Expected outcome: the destination element should not contain any unsafe attributes

        // Test copySafeNodes method with a source element containing both unsafe tags and attributes
        // Expected outcome: the destination element should not contain any unsafe tags or attributes
    }

    @Test
    public void testCreateSafeElement() {
        // Test createSafeElement method with a source element containing only safe attributes
        // Expected outcome: the destination element should contain all the safe attributes

        // Test createSafeElement method with a source element containing unsafe attributes
        // Expected outcome: the destination element should not contain any unsafe attributes

        // Test createSafeElement method with a source element containing both safe and unsafe attributes
        // Expected outcome: the destination element should contain all the safe attributes and discard the unsafe ones
    }
}

