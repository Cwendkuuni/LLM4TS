package org.jsoup.select;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        Evaluator evaluator = QueryParser.parse("");
        Assert.assertEquals(Evaluator.class, evaluator.getClass());
    }

    @Test
    public void testParseSingleTagSelector() {
        String query = "p";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.Tag);
        Assert.assertEquals(evaluator.getClass(), Evaluator.Tag.class);
        Assert.assertEquals("p", ((Evaluator.Tag) evaluator).getTagName());
    }

    @Test
    public void testParseSingleClassSelector() {
        String query = ".class1";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.Class);
        Assert.assertEquals(evaluator.getClass(), Evaluator.Class.class);
        Assert.assertEquals("class1", ((Evaluator.Class) evaluator).getClassName());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#id1";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.Id);
        Assert.assertEquals(evaluator.getClass(), Evaluator.Id.class);
        Assert.assertEquals("id1", ((Evaluator.Id) evaluator).getId());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "p > div.class1";
        Evaluator evaluator = QueryParser.parse(query);
        CombiningEvaluator and = (CombiningEvaluator) evaluator;
        Evaluator tag = and.getLeftEvaluator();
        Evaluator classEvaluator = and.getRightEvaluator();

        Assert.assertTrue(tag instanceof Evaluator.Tag);
        Assert.assertEquals(tag.getClass(), Evaluator.Tag.class);
        Assert.assertEquals("p", ((Evaluator.Tag) tag).getTagName());

        Assert.assertTrue(classEvaluator instanceof Evaluator.Class);
        Assert.assertEquals(classEvaluator.getClass(), Evaluator.Class.class);
        Assert.assertEquals("class1", ((Evaluator.Class) classEvaluator).getClassName());
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[href]";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.Attribute);
        Assert.assertEquals(evaluator.getClass(), Evaluator.Attribute.class);
        Assert.assertEquals("href", ((Evaluator.Attribute) evaluator).getAttributeName());
    }

    @Test
    public void testParseAttributeWithValueSelector() {
        String query = "[href='http://example.com']";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        Assert.assertEquals(evaluator.getClass(), Evaluator.AttributeWithValue.class);
        Assert.assertEquals("href", ((Evaluator.AttributeWithValue) evaluator).getAttributeName());
        Assert.assertEquals("http://example.com", ((Evaluator.AttributeWithValue) evaluator).getValue());
    }

    @Test
    public void testParseCombinedAttributeSelectors() {
        String query = "[href='http://example.com'] > p";
        Evaluator evaluator = QueryParser.parse(query);
        CombiningEvaluator and = (CombiningEvaluator) evaluator;
        Evaluator attributeEvaluator = and.getLeftEvaluator();
        Evaluator tagEvaluator = and.getRightEvaluator();

        Assert.assertTrue(attributeEvaluator instanceof Evaluator.AttributeWithValue);
        Assert.assertEquals(attributeEvaluator.getClass(), Evaluator.AttributeWithValue.class);
        Assert.assertEquals("href", ((Evaluator.AttributeWithValue) attributeEvaluator).getAttributeName());
        Assert.assertEquals("http://example.com", ((Evaluator.AttributeWithValue) attributeEvaluator).getValue());

        Assert.assertTrue(tagEvaluator instanceof Evaluator.Tag);
        Assert.assertEquals(tagEvaluator.getClass(), Evaluator.Tag.class);
        Assert.assertEquals("p", ((Evaluator.Tag) tagEvaluator).getTagName());
    }

    @Test
    public void testParseNthChildSelector() {
        String query = "li:nth-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        NthChildEvaluator nthChildEvaluator = (NthChildEvaluator) evaluator;
        Assert.assertEquals(2, nthChildEvaluator.getNthChildIndex());
        Assert.assertEquals("li", nthChildEvaluator.getTagName());
    }

    @Test
    public void testParseNthOfTypeSelector() {
        String query = "li:nth-of-type(2)";
        Evaluator evaluator = QueryParser.parse(query);
        NthOfTypeEvaluator nthOfTypeEvaluator = (NthOfTypeEvaluator) evaluator;
        Assert.assertEquals(2, nthOfTypeEvaluator.getNthOfTypeIndex());
        Assert.assertEquals("li", nthOfTypeEvaluator.getTagName());
    }

    @Test
    public void testParseNthLastChildSelector() {
        String query = "li:nth-last-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        NthLastChildEvaluator nthLastChildEvaluator = (NthLastChildEvaluator) evaluator;
        Assert.assertEquals(2, nthLastChildEvaluator.getNthLastChildIndex());
        Assert.assertEquals("li", nthLastChildEvaluator.getTagName());
    }

    @Test
    public void testParseNthLastOfTypeSelector() {
        String query = "li:nth-last-of-type(2)";
        Evaluator evaluator = QueryParser.parse(query);
        NthLastOfTypeEvaluator nthLastOfTypeEvaluator = (NthLastOfTypeEvaluator) evaluator;
        Assert.assertEquals(2, nthLastOfTypeEvaluator.getNthLastOfTypeIndex());
        Assert.assertEquals("li", nthLastOfTypeEvaluator.getTagName());
    }

    @Test
    public void testParsePseudoClassSelector() {
        String query = "li:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.IsFirstChild);
        Assert.assertEquals(evaluator.getClass(), Evaluator.IsFirstChild.class);
    }

    @Test
    public void testParsePseudoClassSelectorWithTag() {
        String query = "li:first-child > ul > li";
        Evaluator evaluator = QueryParser.parse(query);
        CombiningEvaluator and = (CombiningEvaluator) evaluator;
        Evaluator firstChildEvaluator = and.getLeftEvaluator();
        Evaluator tagEvaluator = and.getRightEvaluator();

        Assert.assertTrue(firstChildEvaluator instanceof Evaluator.IsFirstChild);
        Assert.assertEquals(firstChildEvaluator.getClass(), Evaluator.IsFirstChild.class);

        Assert.assertTrue(tagEvaluator instanceof Evaluator.Tag);
        Assert.assertEquals(tagEvaluator.getClass(), Evaluator.Tag.class);
        Assert.assertEquals("li", ((Evaluator.Tag) tagEvaluator).getTagName());
    }

    @Test
    public void testParseCombinedAttributeWithValueAndTagSelectors() {
        String query = "[href='http://example.com'][title='test'] > p";
        Evaluator evaluator = QueryParser.parse(query);
        CombiningEvaluator and = (CombiningEvaluator) evaluator;
        Evaluator attributeEvaluator = and.getLeftEvaluator();
        Evaluator tagEvaluator = and.getRightEvaluator();

        Assert.assertTrue(attributeEvaluator instanceof Evaluator.AttributeWithValue);
        Assert.assertEquals(attributeEvaluator.getClass(), Evaluator.AttributeWithValue.class);
        Assert.assertEquals("href", ((Evaluator.AttributeWithValue) attributeEvaluator).getAttributeName());
        Assert.assertEquals("http://example.com", ((Evaluator.AttributeWithValue) attributeEvaluator).getValue());

        Assert.assertTrue(tagEvaluator instanceof Evaluator.Tag);
        Assert.assertEquals(tagEvaluator.getClass(), Evaluator.Tag.class);
        Assert.assertEquals("p", ((Evaluator.Tag) tagEvaluator).getTagName());
    }

    @Test
    public void testParsePseudoClassSelectorWithAttribute() {
        String query = "li:has(a[href='http://example.com'])";
        Evaluator evaluator = QueryParser.parse(query);
        StructuralEvaluator hasEvaluator = (StructuralEvaluator) evaluator;
        Assert.assertTrue(hasEvaluator instanceof StructuralEvaluator.Has);
        Assert.assertEquals(hasEvaluator.getClass(), StructuralEvaluator.Has.class);

        Evaluator attributeEvaluator = hasEvaluator.getSubSelector();
        Assert.assertTrue(attributeEvaluator instanceof Evaluator.AttributeWithValue);
        Assert.assertEquals(attributeEvaluator.getClass(), Evaluator.AttributeWithValue.class);
        Assert.assertEquals("href", ((Evaluator.AttributeWithValue) attributeEvaluator).getAttributeName());
        Assert.assertEquals("http://example.com", ((Evaluator.AttributeWithValue) attributeEvaluator).getValue());
    }

    @Test
    public void testParsePseudoClassSelectorWithAttributeAndTag() {
        String query = "li:has(a[href='http://example.com']):first-child";
        Evaluator evaluator = QueryParser.parse(query);
        CombiningEvaluator and = (CombiningEvaluator) evaluator;
        Evaluator hasEvaluator = and.getLeftEvaluator();
        Evaluator firstChildEvaluator = and.getRightEvaluator();

        Assert.assertTrue(hasEvaluator instanceof StructuralEvaluator.Has);
        Assert.assertEquals(hasEvaluator.getClass(), StructuralEvaluator.Has.class);

        Evaluator attributeEvaluator = hasEvaluator.getSubSelector();
        Assert.assertTrue(attributeEvaluator instanceof Evaluator.AttributeWithValue);
        Assert.assertEquals(attributeEvaluator.getClass(), Evaluator.AttributeWithValue.class);
        Assert.assertEquals("href", ((Evaluator.AttributeWithValue) attributeEvaluator).getAttributeName());
        Assert.assertEquals("http://example.com", ((Evaluator.AttributeWithValue) attributeEvaluator).getValue());

        Assert.assertTrue(firstChildEvaluator instanceof Evaluator.IsFirstChild);
        Assert.assertEquals(firstChildEvaluator.getClass(), Evaluator.IsFirstChild.class);
    }
}

class NthChildEvaluator extends StructuralEvaluator.Has {
    public NthChildEvaluator(int nthChildIndex, String tagName) {
        super(new Evaluator.Tag(tagName));
        this.nthChildIndex = nthChildIndex;
    }

    int nthChildIndex;

    @Override
    protected Evaluator getTargetElement(Element parent) {
        List<Element> children = parent.children();
        return children.get(nthChildIndex - 1);
    }
}

class NthOfTypeEvaluator extends StructuralEvaluator.Has {
    public NthOfTypeEvaluator(int nthOfTypeIndex, String tagName) {
        super(new Evaluator.Tag(tagName));
        this.nthOfTypeIndex = nthOfTypeIndex;
    }

    int nthOfTypeIndex;

    @Override
    protected Evaluator getTargetElement(Element parent) {
        int index = 0;
        Element current = parent;
        while (current != null) {
            if (current.tagName().equals(tagName)) {
                if (++index == nthOfTypeIndex) {
                    return current;
                }
            }
            current = current.previousSibling();
        }
        throw new ElementNotFoundException();
    }
}

class NthLastChildEvaluator extends StructuralEvaluator.Has {
    public NthLastChildEvaluator(int nthLastChildIndex, String tagName) {
        super(new Evaluator.Tag(tagName));
        this.nthLastChildIndex = nthLastChildIndex;
    }

    int nthLastChildIndex;

    @Override
    protected Evaluator getTargetElement(Element parent) {
        List<Element> children = parent.children();
        return children.get(children.size() - nthLastChildIndex);
    }
}

class NthLastOfTypeEvaluator extends StructuralEvaluator.Has {
    public NthLastOfTypeEvaluator(int nthLastOfTypeIndex, String tagName) {
        super(new Evaluator.Tag(tagName));
        this.nthLastOfTypeIndex = nthLastOfTypeIndex;
    }

    int nthLastOfTypeIndex;

    @Override
    protected Evaluator getTargetElement(Element parent) {
        List<Element> siblings = parent.elementsSibling();
        return siblings.get(siblings.size() - nthLastOfTypeIndex);
    }
}