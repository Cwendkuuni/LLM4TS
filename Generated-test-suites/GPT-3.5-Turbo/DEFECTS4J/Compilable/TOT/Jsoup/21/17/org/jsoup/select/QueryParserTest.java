package org.jsoup.select;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

   @Test
   public void testParse() {
      //Test case 1: Typical use case - CSS query without combinators
      String query1 = "div#my-id.my-class[attr]";
      Evaluator evaluator1 = QueryParser.parse(query1);
      assertNotNull(evaluator1);
      
      //Test case 2: Typical use case - CSS query with combinators
      String query2 = "div > span";
      Evaluator evaluator2 = QueryParser.parse(query2);
      assertNotNull(evaluator2);

      //Test case 3: Edge case - CSS query with complex selectors
      String query3 = "div span > .my-class";
      Evaluator evaluator3 = QueryParser.parse(query3);
      assertNotNull(evaluator3);

      //Test case 4: Error scenario - Invalid CSS query
      String query4 = "div.[attr=invalid]";
      try {
         Evaluator evaluator4 = QueryParser.parse(query4);
         fail("Expected SelectorParseException");
      } catch (Selector.SelectorParseException e) {
         // Exception expected
      }
   }

   @Test
   public void testParse_commaSeparation() {
      //Test case 1: Typical use case - CSS query with comma-separated selectors
      String query1 = "div, span, .my-class";
      Evaluator evaluator1 = QueryParser.parse(query1);
      assertNotNull(evaluator1);

      //Test case 2: Edge case - CSS query with empty sub-queries
      String query2 = "div, , span, .my-class";
      Evaluator evaluator2 = QueryParser.parse(query2);
      assertNotNull(evaluator2);

      //Test case 3: Error scenario - Invalid CSS query with comma as last character
      String query3 = "div, span, .";
      try {
         Evaluator evaluator3 = QueryParser.parse(query3);
         fail("Expected SelectorParseException");
      } catch (Selector.SelectorParseException e) {
         // Exception expected
      }
   }

   @Test
   public void testParse_attributeQueries() {
      //Test case 1: Typical use case - CSS query with attribute selectors
      String query1 = "div[attr=value]";
      Evaluator evaluator1 = QueryParser.parse(query1);
      assertNotNull(evaluator1);
      
      //Test case 2: Typical use case - CSS query with different attribute selectors
      String query2 = "div[attr1^=value][attr2~=value]";
      Evaluator evaluator2 = QueryParser.parse(query2);
      assertNotNull(evaluator2);

      //Test case 3: Edge case - Attribute selector without a value
      String query3 = "div[attr]";
      Evaluator evaluator3 = QueryParser.parse(query3);
      assertNotNull(evaluator3);

      //Test case 4: Error scenario - Invalid attribute selector
      String query4 = "div[attr=]";
      try {
         Evaluator evaluator4 = QueryParser.parse(query4);
         fail("Expected SelectorParseException");
      } catch (Selector.SelectorParseException e) {
         // Exception expected
      }
   }

   @Test
   public void testParse_pseudoSelectors() {
      //Test case 1: Typical use case - CSS query with pseudo selector :has
      String query1 = "div:has(span)";
      Evaluator evaluator1 = QueryParser.parse(query1);
      assertNotNull(evaluator1);
      
      //Test case 2: Typical use case - CSS query with pseudo selector :contains
      String query2 = "div:contains(text)";
      Evaluator evaluator2 = QueryParser.parse(query2);
      assertNotNull(evaluator2);

      //Test case 3: Edge case - CSS query with pseudo selector :not
      String query3 = "div:not(.my-class)";
      Evaluator evaluator3 = QueryParser.parse(query3);
      assertNotNull(evaluator3);

      //Test case 4: Error scenario - CSS query with invalid pseudo selector
      String query4 = "div:invalid";
      try {
         Evaluator evaluator4 = QueryParser.parse(query4);
         fail("Expected SelectorParseException");
      } catch (Selector.SelectorParseException e) {
         // Exception expected
      }
   }

   @Test
   public void testParse_errorScenarios() {
      //Test case 1: Error scenario - Empty CSS query
      String query1 = "";
      try {
         Evaluator evaluator1 = QueryParser.parse(query1);
         fail("Expected SelectorParseException");
      } catch (Selector.SelectorParseException e) {
         // Exception expected
      }
      
      //Test case 2: Error scenario - CSS query with invalid tag name
      String query2 = "$div";
      try {
         Evaluator evaluator2 = QueryParser.parse(query2);
         fail("Expected SelectorParseException");
      } catch (Selector.SelectorParseException e) {
         // Exception expected
      }

      //Test case 3: Error scenario - CSS query with invalid attribute operator
      String query3 = "div[attr=*value]";
      try {
         Evaluator evaluator3 = QueryParser.parse(query3);
         fail("Expected SelectorParseException");
      } catch (Selector.SelectorParseException e) {
         // Exception expected
      }
   }
}