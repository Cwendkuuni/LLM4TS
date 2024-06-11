package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        Evaluator evaluator = QueryParser.parse("");
        assertNull(evaluator);
    }

    @Test
    public void testParseSimpleTag() {
        String query = "p";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals(Evaluator.TagType.TAG, evaluator.getType());
        assertEquals("p", evaluator.getData());
    }

    @Test
    public void testParseId() {
        String query = "#id1";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("id1", evaluator.getData());
    }

    @Test
    public void testParseClass() {
        String query = ".class1";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("class1", evaluator.getData());
    }

    @Test
    public void testParseAttribute() {
        String query = "[title=Test]";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("title", evaluator.getData().getTagName());
        assertEquals("Test", evaluator.getData().getValue());
    }

    @Test
    public void testParseAttributeWithOperator() {
        String query = "[title!=Test]";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueNot);
        assertEquals("title", evaluator.getData().getTagName());
        assertEquals("Test", evaluator.getData().getValue());
    }

    @Test
    public void testParseAttributeWithStartValue() {
        String query = "[title^=Test]";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
        assertEquals("title", evaluator.getData().getTagName());
        assertEquals("Test", evaluator.getData().getValue());
    }

    @Test
    public void testParseAttributeWithEndValue() {
        String query = "[title$=Test]";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
        assertEquals("title", evaluator.getData().getTagName());
        assertEquals("Test", evaluator.getData().getValue());
    }

    @Test
    public void testParseAttributeWithContainValue() {
        String query = "[title*=Test]";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
        assertEquals("title", evaluator.getData().getTagName());
        assertEquals("Test", evaluator.getData().getValue());
    }

    @Test
    public void testParseAttributeWithMatchValue() {
        String query = "[title~=Test]";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        assertEquals("title", evaluator.getData().getTagName());
        assertTrue(((Evaluator.AttributeWithValueMatching) evaluator).getPattern().matcher("Test Title").matches());
    }

    @Test
    public void testParseMultipleSelectors() {
        String query = "p#id1.class1[title=Test]";
        Evaluator evaluator = QueryParser.parse(query);

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag(TagType.TAG, "p"));
        expectedEvaluators.add(new Evaluator.Id(IdType.ID, "id1"));
        expectedEvaluators.add(new Evaluator.Class(ClassType.CLASS, "class1"));
        expectedEvaluators.add(new Evaluator.Attribute(AttributeType.ATTR, "title", "Test"));

        Evaluator expectedCombiningEvaluator = new CombiningEvaluator.And(expectedEvaluators.toArray(new Evaluator[0]));

        assertTrue(evaluator instanceof CombiningEvaluator.And);
        Evaluator actualCombiningEvaluator = evaluator;

        List<Evaluator> actualEvaluators = new ArrayList<>();
        while (actualCombiningEvaluator instanceof CombiningEvaluator) {
            actualCombiningEvaluator = ((CombiningEvaluator) actualCombiningEvaluator).getLeft();
            actualEvaluators.add(actualCombiningEvaluator);
        }

        assertEquals(expectedEvaluators, actualEvaluators);
    }

    @Test
    public void testParseDescendantSelector() {
        String query = "div p";
        Evaluator evaluator = QueryParser.parse(query);

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.Descendant(new Evaluator.Tag(TagType.TAG, "div")));
        expectedEvaluators.add(new Evaluator.Tag(TagType.TAG, "p"));

        Evaluator expectedCombiningEvaluator = new CombiningEvaluator.And(expectedEvaluators.toArray(new Evaluator[0]));

        assertTrue(evaluator instanceof CombiningEvaluator.And);
        Evaluator actualCombiningEvaluator = evaluator;

        List<Evaluator> actualEvaluators = new ArrayList<>();
        while (actualCombiningEvaluator instanceof CombiningEvaluator) {
            actualCombiningEvaluator = ((CombiningEvaluator) actualCombiningEvaluator).getLeft();
            actualEvaluators.add(actualCombiningEvaluator);
        }

        assertEquals(expectedEvaluators, actualEvaluators);
    }

    @Test
    public void testParseAdjacentSiblingSelector() {
        String query = "p + h1";
        Evaluator evaluator = QueryParser.parse(query);

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.AdjacentSibling(new Evaluator.Tag(TagType.TAG, "p")));
        expectedEvaluators.add(new Evaluator.Tag(TagType.TAG, "h1"));

        Evaluator expectedCombiningEvaluator = new CombiningEvaluator.And(expectedEvaluators.toArray(new Evaluator[0]));

        assertTrue(evaluator instanceof CombiningEvaluator.And);
        Evaluator actualCombiningEvaluator = evaluator;

        List<Evaluator> actualEvaluators = new ArrayList<>();
        while (actualCombiningEvaluator instanceof CombiningEvaluator) {
            actualCombiningEvaluator = ((CombiningEvaluator) actualCombiningEvaluator).getLeft();
            actualEvaluators.add(actualCombiningEvaluator);
        }

        assertEquals(expectedEvaluators, actualEvaluators);
    }

    @Test
    public void testParseGeneralSiblingSelector() {
        String query = "p ~ h1";
        Evaluator evaluator = QueryParser.parse(query);

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.GeneralSibling(new Evaluator.Tag(TagType.TAG, "p")));
        expectedEvaluators.add(new Evaluator.Tag(TagType.TAG, "h1"));

        Evaluator expectedCombiningEvaluator = new CombiningEvaluator.And(expectedEvaluators.toArray(new Evaluator[0]));

        assertTrue(evaluator instanceof CombiningEvaluator.And);
        Evaluator actualCombiningEvaluator = evaluator;

        List<Evaluator> actualEvaluators = new ArrayList<>();
        while (actualCombiningEvaluator instanceof CombiningEvaluator) {
            actualCombiningEvaluator = ((CombiningEvaluator) actualCombiningEvaluator).getLeft();
            actualEvaluators.add(actualCombiningEvaluator);
        }

        assertEquals(expectedEvaluators, actualEvaluators);
    }

    @Test
    public void testParsePseudoClassSelector() {
        String query = "p:first-child";
        Evaluator evaluator = QueryParser.parse(query);

        assertTrue(evaluator instanceof Evaluator.PseudoClass);
        assertEquals("first-child", evaluator.getData());
    }

    @Test
    public void testParsePseudoElementSelector() {
        String query = "p:first-letter";
        Evaluator evaluator = QueryParser.parse(query);

        assertTrue(evaluator instanceof Evaluator.PseudoElement);
        assertEquals("first-letter", evaluator.getData());
    }

    @Test
    public void testParseComplexSelector() {
        String query = "div p#id1.class1:first-child:has(a:contains(Link)):not(:has(form))";
        Evaluator evaluator = QueryParser.parse(query);

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.Descendant(new Evaluator.Tag(TagType.TAG, "div")));
        expectedEvaluators.add(new Evaluator.Tag(TagType.TAG, "p"));
        expectedEvaluators.add(new IdSelector("id1"));
        expectedEvaluators.add(new ClassSelector("class1"));
        expectedEvaluators.add(new StructuralEvaluator.FirstChild());
        expectedEvaluators.add(new HasSelector("Link", new TextContainsSelector("Link")));
        expectedEvaluators.add(new StructuralEvaluator.Not(new HasSelector("form", null)));

        Evaluator expectedCombiningEvaluator = new CombiningEvaluator.And(expectedEvaluators.toArray(new Evaluator[0]));

        assertTrue(evaluator instanceof CombiningEvaluator.And);
        Evaluator actualCombiningEvaluator = evaluator;

        List<Evaluator> actualEvaluators = new ArrayList<>();
        while (actualCombiningEvaluator instanceof CombiningEvaluator) {
            actualCombiningEvaluator = ((CombiningEvaluator) actualCombiningEvaluator).getLeft();
            actualEvaluators.add(actualCombiningEvaluator);
        }

        assertEquals(expectedEvaluators, actualEvaluators);
    }
}

class IdSelector extends Evaluator {
    private final String id;

    IdSelector(String id) {
        this.id = id;
    }

    @Override
    public EvaluatorType getType() {
        return EvaluatorType.ID;
    }

    @Override
    public Object getData() {
        return id;
    }
}

class ClassSelector extends Evaluator {
    private final String className;

    ClassSelector(String className) {
        this.className = className;
    }

    @Override
    public EvaluatorType getType() {
        return EvaluatorType.CLASS;
    }

    @Override
    public Object getData() {
        return className;
    }
}