import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.jsoup.nodes.Element;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class AndTest {

    @Test
    public void testMatches() {
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);
        And and = new And(Arrays.asList(evaluator1, evaluator2));
        Element root = Mockito.mock(Element.class);
        Element node = Mockito.mock(Element.class);

        Mockito.when(evaluator1.matches(root, node)).thenReturn(true);
        Mockito.when(evaluator2.matches(root, node)).thenReturn(true);

        assertTrue(and.matches(root, node));
    }
}