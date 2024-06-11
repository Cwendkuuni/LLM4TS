package org.jsoup.select;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {
  
  @Test
  public void testParse() {
    Evaluator ev = QueryParser.parse("div#id.class[attr='value']");
    List<Evaluator> expectedEvals = new ArrayList<>();
    expectedEvals.add(new Evaluator.Tag("div"));
    expectedEvals.add(new Evaluator.Id("id"));
    expectedEvals.add(new Evaluator.Class("class"));
    expectedEvals.add(new Evaluator.AttributeWithValue("attr", "value"));
    Evaluator expectedEvaluator = new CombiningEvaluator.And(expectedEvals);
    
    Assert.assertEquals(expectedEvaluator, ev);
  }
  
  @Test
  public void testParse_invalidQuery() {
    try {
      QueryParser.parse("div#id.class[attr='value'[");
      Assert.fail("Expected SelectorParseException");
    } catch (Selector.SelectorParseException e) {
      // pass
    }
  }
  
  @Test
  public void testParse_combinator() {
    Evaluator ev = QueryParser.parse("a > b + c ~ d");
    List<Evaluator> expectedEvals = new ArrayList<>();
    expectedEvals.add(new Evaluator.Tag("a"));
    expectedEvals.add(new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("b")));
    expectedEvals.add(new StructuralEvaluator.ImmediatePreviousSibling(new Evaluator.Tag("c")));
    expectedEvals.add(new StructuralEvaluator.PreviousSibling(new Evaluator.Tag("d")));
    Evaluator expectedEvaluator = new CombiningEvaluator.And(expectedEvals);
    
    Assert.assertEquals(expectedEvaluator, ev);
  }
  
  @Test
  public void testParse_contains() {
    Evaluator ev = QueryParser.parse(":contains(text)");
    Evaluator expectedEvaluator = new Evaluator.ContainsText("text");
    
    Assert.assertEquals(expectedEvaluator, ev);
  }
  
  @Test
  public void testParse_containsOwn() {
    Evaluator ev = QueryParser.parse(":containsOwn(text)");
    Evaluator expectedEvaluator = new Evaluator.ContainsOwnText("text");
    
    Assert.assertEquals(expectedEvaluator, ev);
  }
  
  @Test
  public void testParse_containsData() {
    Evaluator ev = QueryParser.parse(":containsData(data)");
    Evaluator expectedEvaluator = new Evaluator.ContainsData("data");
    
    Assert.assertEquals(expectedEvaluator, ev);
  }
  
  @Test
  public void testParse_matches() {
    Evaluator ev = QueryParser.parse(":matches(regex)");
    Evaluator expectedEvaluator = new Evaluator.Matches(Pattern.compile("regex"));
    
    Assert.assertEquals(expectedEvaluator, ev);
  }
  
  @Test
  public void testParse_matchesOwn() {
    Evaluator ev = QueryParser.parse(":matchesOwn(regex)");
    Evaluator expectedEvaluator = new Evaluator.MatchesOwn(Pattern.compile("regex"));
  
    Assert.assertEquals(expectedEvaluator, ev);
  }
  
  @Test
  public void testParse_not() {
    Evaluator ev = QueryParser.parse(":not(selector)");
    Evaluator expectedEvaluator = new StructuralEvaluator.Not(new Evaluator.Tag("selector"));
  
    Assert.assertEquals(expectedEvaluator, ev);
  }
}