package org.jsoup.select;

import org.jsoup.helper.ElementAssert;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        List<Evaluator> evaluators = Arrays.asList(
                new ElementStartsEvaluator("div"),
                new AttributeEvaluator("class", "active")
        );

        And evaluator = new And(evaluators);

        Element element = new Element("div");
        element.classes().add("active");

        ElementAssert.assertThat(evaluator).matches(new Element("html"), element);
    }

    @Test
    public void testAndEvaluatorWithNoMatchingElement() {
        List<Evaluator> evaluators = Arrays.asList(
                new ElementStartsEvaluator("div"),
                new AttributeEvaluator("class", "inactive")
        );

        And evaluator = new And(evaluators);

        Element element = new Element("html");

        ElementAssert.assertThat(evaluator).doesNotMatch(new Element("html"), element);
    }

    @Test
    public void testAndEvaluatorWithEmptyList() {
        And evaluator = new And(Collections.emptyList());

        Element element = new Element("html");

        ElementAssert.assertThat(evaluator).matches(new Element("html"), element);
    }

    @Test
    public void testOrEvaluator() {
        List<Evaluator> evaluators1 = Arrays.asList(
                new ElementStartsEvaluator("div"),
                new AttributeEvaluator("class", "active")
        );

        List<Evaluator> evaluators2 = Arrays.asList(
                new ElementStartsEvaluator("span"),
                new AttributeEvaluator("class", "selected")
        );

        Or evaluator = new Or(evaluators1);
        evaluator.addAll(evaluators2);

        Element element1 = new Element("div");
        element1.classes().add("active");

        Element element2 = new Element("span");
        element2.classes().add("selected");

        ElementAssert.assertThat(evaluator).matches(new Element("html"), element1);
        ElementAssert.assertThat(evaluator).matches(new Element("html"), element2);
    }

    @Test
    public void testOrEvaluatorWithNoMatchingElement1() {
        List<Evaluator> evaluators1 = Arrays.asList(
                new ElementStartsEvaluator("div"),
                new AttributeEvaluator("class", "active")
        );

        List<Evaluator> evaluators2 = Arrays.asList(
                new ElementStartsEvaluator("span"),
                new AttributeEvaluator("class", "inactive")
        );

        Or evaluator = new Or(evaluators1);
        evaluator.addAll(evaluators2);

        Element element = new Element("html");

        ElementAssert.assertThat(evaluator).doesNotMatch(new Element("html"), new Element("div"));
        ElementAssert.assertThat(evaluator).doesNotMatch(new Element("html"), new Element("span"));
    }

    @Test
    public void testOrEvaluatorWithNoMatchingElement2() {
        List<Evaluator> evaluators1 = Arrays.asList(
                new ElementStartsEvaluator("div"),
                new AttributeEvaluator("class", "active")
        );

        Or evaluator = new Or(evaluators1);

        Element element = new Element("html");

        ElementAssert.assertThat(evaluator).doesNotMatch(new Element("html"), new Element("div"));
    }
}