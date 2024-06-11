import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CombiningEvaluatorTest {

    private CombiningEvaluator evaluator;
    private Element root;
    private Element node;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new CombiningEvaluator(), null, null},
                {new And(Arrays.asList(new ElementSelector("tag1"))), new Element("tag1"), null},
                {new And(Arrays.asList(new ElementSelector("tag1"), new ClassSelector("class1"))), new Element("tag1").addClass("class1"), null},
                {new And(new ArrayList<>()), null, null},
                {new Or(Arrays.asList(new ElementSelector("tag1"), new ElementSelector("tag2"))), new Element("tag1"), null},
                {new Or(Arrays.asList(new ElementSelector("tag1"), new ElementSelector("tag2"), new ClassSelector("class1"))), new Element("tag1").addClass("class1"), null},
                {new Or(new ArrayList<>()), null, null}
        });
    }

    public CombiningEvaluatorTest(CombiningEvaluator evaluator, Element root, Element node) {
        this.evaluator = evaluator;
        this.root = root;
        this.node = node;
    }

    @Test
    public void testMatches_Basic() {
        assertTrue(evaluator.matches(root, node));
    }

    @Test
    public void testMatches_False() {
        if (node != null) {
            Element wrongNode = new Element("wrongTag");
            assertFalse(evaluator.matches(root, wrongNode));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testMatches_IllegalStateException() {
        And and = new And(Arrays.asList(new ElementSelector("tag1"), new ElementSelector("tag2")));
        and.evaluators.remove(0);
        and.matches(root, node);
    }

    @Test
    public void testMatches_EmptyEvaluators() {
        And and = new And(new ArrayList<>());
        assertFalse(and.matches(root, node));
    }

    @Test
    public void testMatches_OrEmptyEvaluators() {
        Or or = new Or(new ArrayList<>());
        assertFalse(or.matches(root, node));
    }

    @Test
    public void testMatches_OrMoreThanOneEvaluator() {
        Or or = new Or(Arrays.asList(new ElementSelector("tag1"), new ElementSelector("tag2")));
        assertTrue(or.matches(root, new Element("tag1")) || or.matches(root, new Element("tag2")));
    }

    @Test
    public void testMatches_AndMultipleEvaluators() {
        And and = new And(Arrays.asList(new ElementSelector("tag1"), new ClassSelector("class1")));
        Element tag1Class1 = new Element("tag1").addClass("class1");
        assertTrue(and.matches(root, tag1Class1));
    }

    @Test
    public void testToString() {
        And and = new And(Arrays.asList(new ElementSelector("tag1"), new ElementSelector("tag2")));
        assertTrue(and.toString().equals("tag1 tag2"));

        Or or = new Or(Arrays.asList(new ElementSelector("tag1"), new ElementSelector("tag2"), new ClassSelector("class1")));
        assertTrue(or.toString().equals(":or tag1 tag2 class1"));
    }
}