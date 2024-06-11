import org.junit.Test;
import org.jsoup.parser.QueryParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QueryParserTest {

 private String query1 = "div.class1 > p:first-child";
 private String query2 = "div#id1 > p:last-child";
 private String query3 = "div[class='class1']:contains(text)";
 private String query4 = "div[class='class1']:not(:contains(text))";
 private String query5 = "div[class='class1']:containsData(data)";
 private String query6 = "div[class='class1']:matches(regex)";
 private String query7 = "div[class='class1']:matchesOwn(regex)";
 private String query8 = "div[class='class1']:nth-child(3)";
 private String query9 = "div[class='class1']:nth-last-child(3)";
 private String query10 = "div[class='class1']:nth-of-type(3)";
 private String query11 = "div[class='class1']:nth-last-of-type(3)";
 private String query12 = "div[class='class1']:empty";
 private String query13 = "div[class='class1']:root";
 private String query14 = "div[class='class1']:first-child";
 private String query15 = "div[class='class1']:last-child";
 private String query16 = "div[class='class1']:first-of-type";
 private String query17 = "div[class='class1']:last-of-type";
 private String query18 = "div[class='class1']:only-child";
 private String query19 = "div[class='class1']:only-of-type";

 @Test
 public void testParseQuery1() {
 QueryParser parser = new QueryParser(query1);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery2() {
 QueryParser parser = new QueryParser(query2);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery3() {
 QueryParser parser = new QueryParser(query3);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidQuery1() {
 QueryParser parser = new QueryParser("div");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidQuery2() {
 QueryParser parser = new QueryParser("div class1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidQuery3() {
 QueryParser parser = new QueryParser("div[class'class1']");
 }

 @Test
 public void testParseQuery4() {
 QueryParser parser = new QueryParser(query4);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery5() {
 QueryParser parser = new QueryParser(query5);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery6() {
 QueryParser parser = new QueryParser(query6);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery7() {
 QueryParser parser = new QueryParser(query7);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery8() {
 QueryParser parser = new QueryParser(query8);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery9() {
 QueryParser parser = new QueryParser(query9);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery10() {
 QueryParser parser = new QueryParser(query10);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery11() {
 QueryParser parser = new QueryParser(query11);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery12() {
 QueryParser parser = new QueryParser(query12);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery13() {
 QueryParser parser = new QueryParser(query13);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery14() {
 QueryParser parser = new QueryParser(query14);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery15() {
 QueryParser parser = new QueryParser(query15);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery16() {
 QueryParser parser = new QueryParser(query16);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery17() {
 QueryParser parser = new QueryParser(query17);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery18() {
 QueryParser parser = new QueryParser(query18);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery19() {
 QueryParser parser = new QueryParser(query19);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery20() {
 QueryParser parser = new QueryParser("div[class='class1']:empty:first-child");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery21() {
 QueryParser parser = new QueryParser("div[class='class1']:root > p:last-child");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery22() {
 QueryParser parser = new QueryParser("div[class='class1']:not(:first-child):last-child");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery23() {
 QueryParser parser = new QueryParser("div:not([class='class1']) > p:first-child");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery24() {
 QueryParser parser = new QueryParser("div[class='class1']:has(p:contains(text))");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery25() {
 QueryParser parser = new QueryParser("div[class='class1']:containsOwn(text)");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery26() {
 QueryParser parser = new QueryParser("div[class='class1']:containsData(data)");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery27() {
 QueryParser parser = new QueryParser("div[class='class1']:matches(regex)");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery28() {
 QueryParser parser = new QueryParser("div[class='class1']:matchesOwn(regex)");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery29() {
 QueryParser parser = new QueryParser("div[class='class1']:nth-child(3):not(:last-child)");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery30() {
 QueryParser parser = new QueryParser("div[class='class1']:nth-last-child(3):not(:first-child)");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery31() {
 QueryParser parser = new QueryParser("div[class='class1']:nth-of-type(3):not(:last-of-type)");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery32() {
 QueryParser parser = new QueryParser("div[class='class1']:nth-last-of-type(3):not(:first-of-type)");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery33() {
 QueryParser parser = new QueryParser("div[class='class1']:only-child:has(p:contains(text))");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseQuery34() {
 QueryParser parser = new QueryParser("div[class='class1']:only-of-type:has(p:contains(text))");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }
}