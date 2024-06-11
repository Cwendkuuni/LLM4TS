package org.jsoup.select;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.parser.TokenQueue;

public class QueryParserTest {

  @Test
  public void testParse() {
    List<Evaluator> evals = new ArrayList<Evaluator>();
    evals.add(new Evaluator.AllElements());
    Evaluator.And andEvaluator = new Evaluator.And(evals);
    assertTrue(andEvaluator instanceof Evaluator);
  }

  @Test
  public void testCombinator() {
    List<Evaluator> evals = new ArrayList<Evaluator>();
    evals.add(new Evaluator.AllElements());
    Evaluator.And andEvaluator = new Evaluator.And(evals);
    TokenQueue tq = new TokenQueue("query");
    QueryParser parser = new QueryParser(tq.toString());
    parser.combinator('>');
    assertTrue(parser.evals.get(0) instanceof Evaluator.And);
  }

  @Test
  public void testConsumeSubQuery() {
    TokenQueue tq = new TokenQueue("query");
    QueryParser parser = new QueryParser(tq.toString());
    String subQuery = parser.consumeSubQuery();
    assertEquals("query", subQuery);
  }

  @Test
  public void testFindElements() {
    List<Evaluator> evals = new ArrayList<Evaluator>();
    evals.add(new Evaluator.AllElements());
    Evaluator.And andEvaluator = new Evaluator.And(evals);
    TokenQueue tq = new TokenQueue("query");
    QueryParser parser = new QueryParser(tq.toString());
    parser.findElements();
    assertTrue(parser.evals.get(0) instanceof Evaluator.AllElements);
  }

  @Test
  public void testById() {
    TokenQueue tq = new TokenQueue("query");
    QueryParser parser = new QueryParser(tq.toString());
    parser.byId();
    assertTrue(parser.evals.get(0) instanceof Evaluator.Id);
  }

  @Test
  public void testByClass() {
    TokenQueue tq = new TokenQueue("query");
    QueryParser parser = new QueryParser(tq.toString());
    parser.byClass();
    assertTrue(parser.evals.get(0) instanceof Evaluator.Class);
  }

  @Test
  public void testByTag() {
    TokenQueue tq = new TokenQueue("query");
    QueryParser parser = new QueryParser(tq.toString());
    parser.byTag();
    assertTrue(parser.evals.get(0) instanceof Evaluator.Tag);
  }

  @Test
  public void testByAttribute() {
    TokenQueue tq = new TokenQueue("query");
    QueryParser parser = new QueryParser(tq.toString());
    parser.byAttribute();
    assertTrue(parser.evals.get(0) instanceof Evaluator.Attribute);
  }

  @Test
  public void testAllElements() {
    TokenQueue tq = new TokenQueue("query");
    QueryParser parser = new QueryParser(tq.toString());
    parser.allElements();
    assertTrue(parser.evals.get(0) instanceof Evaluator.AllElements);
  }

  @Test
  public void testIndexLessThan() {
    TokenQueue tq = new TokenQueue("query");
    QueryParser parser = new QueryParser(tq.toString());
    parser.indexLessThan();
    assertTrue(parser.evals.get(0) instanceof Evaluator.IndexLessThan);
  }

  @Test
  public void testIndexGreaterThan() {
    TokenQueue tq = new TokenQueue("query");
    QueryParser parser = new QueryParser(tq.toString());
    parser.indexGreaterThan();
    assertTrue(parser.evals.get(0) instanceof Evaluator.IndexGreaterThan);
  }

  @Test
  public void testIndexEquals() {
    TokenQueue tq = new TokenQueue("query");
    QueryParser parser = new QueryParser(tq.toString());
    parser.indexEquals();
    assertTrue(parser.evals.get(0) instanceof Evaluator.IndexEquals);
  }

  @Test
  public void testHas() {
    TokenQueue tq = new TokenQueue("query");
    QueryParser parser = new QueryParser(tq.toString());
    parser.has();
    assertTrue(parser.evals.get(0) instanceof Evaluator.Has);
  }

  @Test
  public void testContains() {
    TokenQueue tq = new TokenQueue("query");
    QueryParser parser = new QueryParser(tq.toString());
    parser.contains(false);
    assertTrue(parser.evals.get(0) instanceof Evaluator.ContainsText);
  }

  @Test
  public void testContainsOwn() {
    TokenQueue tq = new TokenQueue("query");
    QueryParser parser = new QueryParser(tq.toString());
    parser.contains(true);
    assertTrue(parser.evals.get(0) instanceof Evaluator.ContainsOwnText);
  }

  @Test
  public void testMatches() {
    TokenQueue tq = new TokenQueue("query");
    QueryParser parser = new QueryParser(tq.toString());
    parser.matches(false);
    assertTrue(parser.evals.get(0) instanceof Evaluator.Matches);
  }

  @Test
  public void testMatchesOwn() {
    TokenQueue tq = new TokenQueue("query");
    QueryParser parser = new QueryParser(tq.toString());
    parser.matches(true);
    assertTrue(parser.evals.get(0) instanceof Evaluator.MatchesOwn);
  }

  @Test
  public void testNot() {
    TokenQueue tq = new TokenQueue("query");
    QueryParser parser = new QueryParser(tq.toString());
    parser.not();
    assertTrue(parser.evals.get(0) instanceof Evaluator.Not);
  }
}