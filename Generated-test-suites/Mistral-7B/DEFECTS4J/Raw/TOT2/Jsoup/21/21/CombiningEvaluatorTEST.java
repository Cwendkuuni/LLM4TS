import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class AndTest {
    private And and;
    private List<Evaluator> evaluators;
    private Element element;

    @Parameterized.Parameters(name = "{index}: evaluator[{0}]")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new ElementMock("element1")},
                {new ElementMock("element2")},
                {new ElementMock("element3")}
        });
    }

    public AndTest(Evaluator evaluator) {
        this.evaluators = Arrays.asList(evaluator);
        this.and = new And(this.evaluators);
        this.element = Mockito.mock(Element.class);
    }

    @Test
    public void shouldMatchAllEvaluators() {
        for (Evaluator evaluator : this.evaluators) {
            Mockito.when(evaluator.matches(Mockito.eq(this.element), Mockito.any(Element.class)))
                    .thenReturn(true);
        }

        assertTrue(this.and.matches(Mockito.mock(Element.class), this.element));
    }
}

class ElementMock implements Element {
    private String name;

    public ElementMock(String name) {
        this.name = name;
    }

    @Override
    public String tagName() {
        return this.name;
    }
}