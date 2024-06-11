package org.jsoup.select;

import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.ElementsSelector;
import org.jsoup.select.Evaluator;
import static org.junit.Assert.*;

public class CombiningEvaluatorTest {
    @Test
    public void testAndEvaluator() {
        // Given
        List<Evaluator> evaluators = Arrays.asList(
                new ElementsSelector("p"),
                new ElementsSelector("span")
        );
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(evaluators);
        Element element = new Element("div");
        element.appendText("This is a paragraph p.");
        element.append(new Element("p"));
        element.append(new Element("span"));

        // When
        boolean matches = andEvaluator.matches(new Element("html"), element);

        // Then
        assertTrue(matches);
    }

    @Test
    public void testOrEvaluator() {
        // Given
        List<Evaluator> evaluators = Arrays.asList(
                new ElementsSelector("p"),
                new ElementsSelector("span")
        );
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(evaluators);
        Element element = new Element("div");
        element.appendText("This is a paragraph p.");
        element.append(new Element("p"));

        // When
        boolean matches = orEvaluator.matches(new Element("html"), element);

        // Then
        assertTrue(matches);
    }

    @Test
    public void testOrEvaluatorWithFalse() {
        // Given
        List<Evaluator> evaluators = Arrays.asList(
                new ElementsSelector("p"),
                new ElementsSelector("span")
        );
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(evaluators);
        Element element = new Element("div");

        // When
        boolean matches = orEvaluator.matches(new Element("html"), element);

        // Then
        assertFalse(matches);
    }
}