package org.jsoup.select.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.helper.StringUtil;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for the QueryParser class.
 */
public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertThat(evaluator, instanceOf(Evaluator.Tag.class));
        assertThat(((Evaluator.Tag) evaluator).tagName(), is("div"));
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "div[class='test']";
        Evaluator evaluator = QueryParser.parse(query);
        assertThat(evaluator, instanceOf(Evaluator.Tag.class));
        assertThat(((Evaluator.Tag) evaluator).tagName(), is("div"));
        assertThat(((Evaluator.Class) ((Evaluator.Tag) evaluator).getClassValue("class")).getValue(), is("test"));
    }

    @Test
    public void testParseIdSelector() {
        String query = "#testId";
        Evaluator evaluator = QueryParser.parse(query);
        assertThat(evaluator, instanceOf(Evaluator.Id.class));
        assertThat(((Evaluator.Id) evaluator).getId(), is("testId"));
    }

    @Test
    public void testParseClassSelector() {
        String query = ".test";
        Evaluator evaluator = QueryParser.parse(query);
        assertThat(evaluator, instanceOf(Evaluator.Class.class));
        assertThat(((Evaluator.Class) evaluator).className(), is("test"));
    }

    @Test
    public void testParseComplexSelector() {
        String query = "#testId > div.test:containsOwn(text)";
        Evaluator evaluator = QueryParser.parse(query);
        assertThat(evaluator, instanceOf(CombiningEvaluator.And.class));
        assertThat(((CombiningEvaluator.And) evaluator).getEvaluators().get(0), instanceOf(Evaluator.Id.class));
        assertThat(((Evaluator.Id) ((CombiningEvaluator.And) evaluator).getEvaluators().get(0)).getId(), is("testId"));
        assertThat(((CombiningEvaluator.And) evaluator).getEvaluators().get(1), instanceOf(StructuralEvaluator.Has.class));
        assertThat(((StructuralEvaluator.Has) ((CombiningEvaluator.And) evaluator).getEvaluators().get(1)).getChildEvaluator(), instanceOf(Evaluator.Tag.class));
        assertThat(((Evaluator.Tag) ((StructuralEvaluator.Has) ((CombiningEvaluator.And) evaluator).getEvaluators().get(1)).getChildEvaluator()).tagName(), is("div"));
        assertThat(((StructuralEvaluator.Has) ((CombiningEvaluator.And) evaluator).getEvaluators().get(1)).getParentEvaluator(), nullValue());
        assertThat(((StructuralEvaluator.Has) ((CombiningEvaluator.And) evaluator).getEvaluators().get(1)).getContainsEvaluator(), instanceOf(Evaluator.ContainsOwnText.class));
        assertThat(((Evaluator.ContainsOwnText) ((StructuralEvaluator.Has) ((CombiningEvaluator.And) evaluator).getEvaluators().get(1)).getContainsEvaluator()).getText(), is("text"));
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        String query = "div#testId:not(#testId2 > div[class='test'])";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseEmptyQuery() {
        String query = "";
        QueryParser.parse(query);
    }
}