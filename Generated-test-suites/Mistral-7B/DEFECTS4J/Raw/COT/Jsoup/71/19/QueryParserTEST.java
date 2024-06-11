package org.jsoup.select; // Import necessary packages if needed

import org.junit.Test;
import org.junit.Assert.*;

public class QueryParserTest {
	// 

	@Test
	public void testParseEmptyQuery() {
		String query = "";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertNull(evaluator);
	}

	@Test
	public void testParseSimpleTagSelector() {
		String query = "div";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof Evaluator.Tag);
	}

	@Test
	public void testParseClassSelector() {
		String query = ".class";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof Evaluator.Class);
	}

	@Test
	public void testParseIdSelector() {
		String query = "#id";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof Evaluator.Id);
	}

	@Test(expected = Selector.SelectorParseException.class)
	public void testParseInvalidSelector() {
		String query = "invalid";
		QueryParser.parse(query);
	}

	@Test
	public void testParseAttributeSelector() {
		String query = "[attr=value]";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof Evaluator.Attribute);
		Assert.assertEquals("attr", evaluator.toString());
	}

	@Test
	public void testParseAttributeWithValueSelector() {
		String query = "[attr=value]";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator attributeEval = null;
		if (evaluator instanceof Evaluator.CombiningEvaluator.And) {
			Evaluator[] andChildren = ((Evaluator.CombiningEvaluator.And) evaluator).getChildren();
			for (Evaluator child : andChildren) {
				if (child instanceof Evaluator.Attribute) {
					attributeEval = child;
					break;
				}
			}
		}
		Assert.assertNotNull(attributeEval);
		Assert.assertEquals("attr", attributeEval.toString().split(" ")[0]);
		Assert.assertEquals("value", attributeEval.toString().split(" ")[1]);
	}

	@Test
	public void testParseAttributeWithValueStartingSelector() {
		String query = "[attr^=value]";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
		Assert.assertEquals("attr", evaluator.toString().split(" ")[0]);
		Assert.assertEquals("value", evaluator.toString().split(" ")[1]);
	}

	@Test
	public void testParseAttributeWithValueEndingSelector() {
		String query = "[attr$=value]";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
		Assert.assertEquals("attr", evaluator.toString().split(" ")[0]);
		Assert.assertEquals("value", evaluator.toString().split(" ")[1]);
	}

	@Test
	public void testParseAttributeWithValueContainingSelector() {
		String query = "[attr*=value]";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
		Assert.assertEquals("attr", evaluator.toString().split(" ")[0]);
		Assert.assertEquals("value", evaluator.toString().split(" ")[1]);
	}

	@Test
	public void testParseAttributeWithValueMatchingSelector() {
		String query = "[attr~=value]";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
		Assert.assertEquals("attr", evaluator.toString().split(" ")[0]);
		Assert.assertNotNull(((Evaluator.AttributeWithValueMatching) evaluator).getPattern());
	}

	@Test
	public void testParsePseudoClassSelector() {
		String query = ":empty";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof Evaluator.IsEmpty);
	}

	@Test
	public void testParseNthChildSelector() {
		String query = ":nth-child(2)";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof Evaluator.IsNthChild);
		Assert.assertEquals(1, ((Evaluator.IsNthChild) evaluator).getIndex());
		Assert.assertEquals(0, ((Evaluator.IsNthChild) evaluator).isOfType());
	}

	@Test
	public void testParseNthLastChildSelector() {
		String query = ":nth-last-child(2)";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
		Assert.assertEquals(1, ((Evaluator.IsNthLastChild) evaluator).getIndex());
		Assert.assertEquals(0, ((Evaluator.IsNthLastChild) evaluator).isOfType());
	}

	@Test
	public void testParseNthOfTypeSelector() {
		String query = ":nth-of-type(2)";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof Evaluator.IsNthOfType);
		Assert.assertEquals(1, ((Evaluator.IsNthOfType) evaluator).getIndex());
		Assert.assertEquals(1, ((Evaluator.IsNthOfType) evaluator).isOfType());
	}

	@Test
	public void testParseNthLastOfTypeSelector() {
		String query = ":nth-last-of-type(2)";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
		Assert.assertEquals(1, ((Evaluator.IsNthLastOfType) evaluator).getIndex());
		Assert.assertEquals(1, ((Evaluator.IsNthLastOfType) evaluator).isOfType());
	}

	@Test
	public void testParseHasSelector() {
		String query = ":has(p)";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof StructuralEvaluator.Has);
	}

	@Test
	public void testParseContainsTextSelector() {
		String query = ":contains(text)";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof Evaluator.ContainsText);
		Assert.assertEquals("text", evaluator.toString());
	}

	@Test
	public void testParseContainsOwnTextSelector() {
		String query = ":containsOwn(text)";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
		Assert.assertEquals("text", evaluator.toString());
	}

	@Test
	public void testParseContainsDataSelector() {
		String query = ":containsData(data)";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof Evaluator.ContainsData);
		Assert.assertEquals("data", evaluator.toString());
	}

	@Test
	public void testParseNotSelector() {
		String query = ":not(p)";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertEquals(Evaluator.class, evaluator.getClass());
		Assert.assertTrue(evaluator instanceof StructuralEvaluator.Not);
	}

	// 
}