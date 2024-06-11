import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Evaluator;
import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParse_EmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        Assert.assertEquals("Expected empty Evaluator tree for empty query.", evaluator, Evaluator.EMPTY_EVALUATOR);
    }

    @Test
    public void testParse_TagSelector() {
        QueryParser parser = new QueryParser("div");
        Evaluator evaluator = parser.parse();
        Assert.assertEquals("Expected tag selector Evaluator for query 'div'.", evaluator.getClass(), Evaluator.Tag.class);
        Assert.assertEquals("Expected tag name 'div' for tag selector Evaluator.", evaluator.toString(), "tag=\"div\"");
    }

    @Test
    public void testParse_ClassSelector() {
        QueryParser parser = new QueryParser(".class");
        Evaluator evaluator = parser.parse();
        Assert.assertEquals("Expected class selector Evaluator for query '.class'.", evaluator.getClass(), Evaluator.Class.class);
        Assert.assertEquals("Expected class name 'class' for class selector Evaluator.", evaluator.toString(), "class=\"class\"");
    }

    @Test
    public void testParse_IdSelector() {
        QueryParser parser = new QueryParser("#id");
        Evaluator evaluator = parser.parse();
        Assert.assertEquals("Expected id selector Evaluator for query '#id'.", evaluator.getClass(), Evaluator.Id.class);
        Assert.assertEquals("Expected id name 'id' for id selector Evaluator.", evaluator.toString(), "id=\"id\"");
    }

    @Test
    public void testParse_CombinedSelectors() {
        QueryParser parser = new QueryParser("div.class");
        Evaluator evaluator = parser.parse();
        Assert.assertEquals("Expected combined Evaluator for query 'div.class'.", evaluator.getClass(), CombiningEvaluator.And.class);
        Assert.assertEquals("Expected tag selector Evaluator for tag 'div'.", evaluator.getChildEvaluator(0).getClass(), Evaluator.Tag.class);
        Assert.assertEquals("Expected class selector Evaluator for class 'class'.", evaluator.getChildEvaluator(1).getClass(), Evaluator.Class.class);
    }

    @Test
    public void testParse_DescendantSelectors() {
        QueryParser parser = new QueryParser("div ul li");
        Evaluator evaluator = parser.parse();
        Assert.assertEquals("Expected descendant Evaluator for query 'div ul li'.", evaluator.getClass(), StructuralEvaluator.Descendant.class);
    }

    @Test
    public void testParse_ChildSelectors() {
        QueryParser parser = new QueryParser("div > ul > li");
        Evaluator evaluator = parser.parse();
        Assert.assertEquals("Expected child Evaluator for query 'div > ul > li'.", evaluator.getClass(), StructuralEvaluator.Child.class);
    }

    @Test
    public void testParse_AdjacentSiblingSelectors() {
        QueryParser parser = new QueryParser("div > p + ul > li");
        Evaluator evaluator = parser.parse();
        Assert.assertEquals("Expected adjacent sibling Evaluator for query 'div > p + ul > li'.", evaluator.getClass(), StructuralEvaluator.AdjacentSibling.class);
    }

    @Test
    public void testParse_GeneralSiblingSelectors() {
        QueryParser parser = new QueryParser("div > p ~ ul > li");
        Evaluator evaluator = parser.parse();
        Assert.assertEquals("Expected general sibling Evaluator for query 'div > p ~ ul > li'.", evaluator.getClass(), StructuralEvaluator.GeneralSibling.class);
    }

    @Test
    public void testParse_AttributeSelectors() {
        QueryParser parser = new QueryParser("[href]");
        Evaluator evaluator = parser.parse();
        Assert.assertEquals("Expected attribute Evaluator for query '[href]'.", evaluator.getClass(), Evaluator.Attribute.class);
        Assert.assertArrayEquals("Expected empty attribute values for query '[href]'.", evaluator.getAttributeValues(), new String[0]);

        parser = new QueryParser("[href=\"mailto:example@example.com\"]");
        evaluator = parser.parse();
        Assert.assertEquals("Expected attribute Evaluator for query '[href=\"mailto:example@example.com\"]'.", evaluator.getClass(), Evaluator.Attribute.class);
        Assert.assertArrayEquals("Expected attribute value 'mailto:example@example.com' for query '[href=\"mailto:example@example.com\"]'.", evaluator.getAttributeValues(), new String[]{"mailto:example@example.com"});

        parser = new QueryParser("[href^=\"mailto:\"]");
        evaluator = parser.parse();
        Assert.assertEquals("Expected attribute selectors with prefix Evaluator for query '[href^=\"mailto:\"]'.", evaluator.getClass(), Evaluator.AttributeStarting.class);
        Assert.assertEquals("Expected prefix 'mailto:' for attribute selectors with prefix Evaluator.", evaluator.toString(), "attribute^=\"href\" prefix=\"mailto:\"");

        parser = new QueryParser("[href$=\".pdf\"]");
        evaluator = parser.parse();
        Assert.assertEquals("Expected attribute selectors with suffix Evaluator for query '[href$=\".pdf\"]'.", evaluator.getClass(), Evaluator.AttributeEnding.class);
        Assert.assertEquals("Expected suffix '.pdf' for attribute selectors with suffix Evaluator.", evaluator.toString(), "attribute$=\"href\" suffix=\".pdf\"");

        parser = new QueryParser("[href*=\"example\"]");
        evaluator = parser.parse();
        Assert.assertEquals("Expected attribute selectors with substring Evaluator for query '[href*=\"example\"]'.", evaluator.getClass(), Evaluator.AttributeWithValueContaining.class);
        Assert.assertEquals("Expected substring 'example' for attribute selectors with substring Evaluator.", evaluator.toString(), "attribute*=\"href\" containsSubstring=\"example\"");
    }

    @Test
    public void testParse_PseudoClasses() {
        QueryParser parser = new QueryParser(":link");
        Evaluator evaluator = parser.parse();
        Assert.assertEquals("Expected link pseudo-class Evaluator for query ':link'.", evaluator.getClass(), Evaluator.PseudoClass.class);
        Assert.assertEquals("Expected 'link' pseudo-class name for link pseudo-class Evaluator.", evaluator.toString(), ":link");

        parser = new QueryParser(":hover");
        evaluator = parser.parse();
        Assert.assertEquals("Expected hover pseudo-class Evaluator for query ':hover'.", evaluator.getClass(), Evaluator.PseudoClass.class);
        Assert.assertEquals("Expected 'hover' pseudo-class name for hover pseudo-class Evaluator.", evaluator.toString(), ":hover");

        parser = new QueryParser(":active");
        evaluator = parser.parse();
        Assert.assertEquals("Expected active pseudo-class Evaluator for query ':active'.", evaluator.getClass(), Evaluator.PseudoClass.class);
        Assert.assertEquals("Expected 'active' pseudo-class name for active pseudo-class Evaluator.", evaluator.toString(), ":active");

        parser = new QueryParser(":focus");
        evaluator = parser.parse();
        Assert.assertEquals("Expected focus pseudo-class Evaluator for query ':focus'.", evaluator.getClass(), Evaluator.PseudoClass.class);
        Assert.assertEquals("Expected 'focus' pseudo-class name for focus pseudo-class Evaluator.", evaluator.toString(), ":focus");

        parser = new QueryParser(":visited");
        evaluator = parser.parse();
        Assert.assertEquals("Expected visited pseudo-class Evaluator for query ':visited'.", evaluator.getClass(), Evaluator.PseudoClass.class);
        Assert.assertEquals("Expected 'visited' pseudo-class name for visited pseudo-class Evaluator.", evaluator.toString(), ":visited");
    }

    @Test
    public void testParse_PseudoElements() {
        QueryParser parser = new QueryParser("::before");
        Evaluator evaluator = parser.parse();
        Assert.assertEquals("Expected pseudo-element 'before' Evaluator for query '::before'.", evaluator.getClass(), Evaluator.PseudoElement.class);
        Assert.assertEquals("Expected 'before' pseudo-element name for pseudo-element Evaluator.", evaluator.toString(), "::before");

        parser = new QueryParser("::after");
        evaluator = parser.parse();
        Assert.assertEquals("Expected pseudo-element 'after' Evaluator for query '::after'.", evaluator.getClass(), Evaluator.PseudoElement.class);
        Assert.assertEquals("Expected 'after' pseudo-element name for pseudo-element Evaluator.", evaluator.toString(), "::after");
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidQuery() {
        QueryParser parser = new QueryParser(":invalid"); // should throw Selector.SelectorParseException
        parser.parse();
    }

    @Test
    public void testParse_Combinators() {
        QueryParser parser = new QueryParser("div > ul + li");
        Evaluator evaluator = parser.parse();
        Assert.assertEquals("Expected combined Evaluator with child, adjacent sibling and general sibling selectors for query 'div > ul + li'.", evaluator.getClass(), CombiningEvaluator.And.class);
        Assert.assertEquals("Expected child Evaluator for tag 'div'.", evaluator.getChildEvaluator(0).getClass(), Evaluator.Tag.class);
        Assert.assertEquals("Expected adjacent sibling Evaluator for query 'ul'.", evaluator.getChildEvaluator(1).getClass(), StructuralEvaluator.AdjacentSibling.class);
        Assert.assertEquals("Expected general sibling Evaluator for query 'li'.", evaluator.getChildEvaluator(2).getClass(), StructuralEvaluator.GeneralSibling.class);
    }

    @Test
    public void testParse_AttributeOperators() {
        QueryParser parser = new QueryParser("[href^=\"mailto:\"][href$=\".com\"]");
        Evaluator evaluator = parser.parse();
        Assert.assertEquals("Expected combined Evaluator for attribute substring prefix and suffix selectors for query '[href^=\"mailto:\"][href$=\".com\"]'.", evaluator.getClass(), CombiningEvaluator.And.class);
        Assert.assertEquals("Expected attribute starting with 'mailto:' Evaluator for query '[href^=\"mailto:\"]'.", evaluator.getChildEvaluator(0).getClass(), Evaluator.AttributeStarting.class);
        Assert.assertEquals("Expected attribute ending with '.com' Evaluator for query '[href$=\".com\"]'.", evaluator.getChildEvaluator(1).getClass(), Evaluator.AttributeEnding.class);
    }

    @Test
    public void testParse_NegatedSelectors() {
        QueryParser parser = new QueryParser(":not(:link)");
        Evaluator evaluator = parser.parse();
        Assert.assertEquals("Expected negated Evaluator for query ':not(:link)'.", evaluator.getClass(), StructuralEvaluator.Not.class);
        Assert.assertEquals("Expected link pseudo-class Evaluator for query ':link'.", evaluator.getChildEvaluator(0).getClass(), Evaluator.PseudoClass.class);
    }

    @Test
    public void testParse_ComplexQuery() {
        QueryParser parser = new QueryParser("ul > li > a[href^=\"http:\"]:hover:focus");
        Evaluator evaluator = parser.parse();
        Assert.assertEquals("Expected combined Evaluator for complex query 'ul > li > a[href^=\"http:\"]:hover:focus'.", evaluator.getClass(), CombiningEvaluator.And.class);
        Assert.assertEquals("Expected child Evaluator for tag 'ul'.", evaluator.getChildEvaluator(0).getClass(), Evaluator.Tag.class);
        Assert.assertEquals("Expected child Evaluator for tag 'li'.", evaluator.getChildEvaluator(1).getClass(), Evaluator.Tag.class);
        Assert.assertEquals("Expected attribute starting with 'http:' Evaluator for query 'a[href^=\"http:\"]'.", evaluator.getChildEvaluator(2).getClass(), Evaluator.AttributeStarting.class);
        Assert.assertEquals("Expected hover pseudo-class Evaluator for query ':hover'.", evaluator.getChildEvaluator(3).getClass(), Evaluator.PseudoClass.class);
        Assert.assertEquals("Expected focus pseudo-class Evaluator for query ':focus'.", evaluator.getChildEvaluator(4).getClass(), Evaluator.PseudoClass.class);
    }
}