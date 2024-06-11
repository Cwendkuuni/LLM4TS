import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryParserTest {

    private QueryParser parser;

    @BeforeEach
    void setUp() {
        parser = new QueryParser("");
    }

    @Test
    void parse_shouldReturnRootEvaluator_whenQueryStartsWithCombinator() {
        Evaluator result = QueryParser.parse(" > div");

        assertTrue(result instanceof StructuralEvaluator.Root);
    }

    @Test
    void parse_shouldReturnAndEvaluator_whenQueryContainsCombiningEvaluatorOr() {
        Evaluator result = QueryParser.parse("div, span");

        assertTrue(result instanceof CombiningEvaluator.And);
        assertEquals(2, ((CombiningEvaluator.And) result).getEvalutors().size());
    }

    @Test
    void parse_shouldReturnAndEvaluatorWithImmediateParent_whenQueryContainsCombinatorGreaterThan() {
        Evaluator result = QueryParser.parse("div > span");

        assertTrue(result instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) result).getEvalutors().get(1) instanceof StructuralEvaluator.ImmediateParent);
    }

    @Test
    void parse_shouldReturnAndEvaluatorWithParent_whenQueryContainsCombinatorSpace() {
        Evaluator result = QueryParser.parse("div  span");

        assertTrue(result instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) result).getEvalutors().get(1) instanceof StructuralEvaluator.Parent);
    }

    @Test
    void parse_shouldReturnAndEvaluatorWithImmediatePreviousSibling_whenQueryContainsCombinatorPlus() {
        Evaluator result = QueryParser.parse("div + span");

        assertTrue(result instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) result).getEvalutors().get(1) instanceof StructuralEvaluator.ImmediatePreviousSibling);
    }

    @Test
    void parse_shouldReturnAndEvaluatorWithPreviousSibling_whenQueryContainsCombinatorTilde() {
        Evaluator result = QueryParser.parse("div ~ span");

        assertTrue(result instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) result).getEvalutors().get(1) instanceof StructuralEvaluator.PreviousSibling);
    }

    @Test
    void parse_shouldReturnAndEvaluatorWithMultipleCombinators_whenQueryContainsMultipleCombinators() {
        Evaluator result = QueryParser.parse("div > span + p");

        assertTrue(result instanceof CombiningEvaluator.And);
        assertEquals(3, ((CombiningEvaluator.And) result).getEvalutors().size());
    }

    @Test
    void parse_shouldReturnAndEvaluatorWithAttributeStarting_whenQueryContainsAttributeStarting() {
        Evaluator result = QueryParser.parse("[data-id^=abc]");

        assertTrue(result instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) result).getEvalutors().get(0) instanceof Evaluator.AttributeStarting);
    }

    @Test
    void parse_shouldReturnAndEvaluatorWithAttribute_whenQueryContainsAttribute() {
        Evaluator result = QueryParser.parse("[data-id]");

        assertTrue(result instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) result).getEvalutors().get(0) instanceof Evaluator.Attribute);
    }

    @Test
    void parse_shouldReturnAndEvaluatorWithAttributeWithValue_whenQueryContainsAttributeWithValue() {
        Evaluator result = QueryParser.parse("[data-id=value]");

        assertTrue(result instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) result).getEvalutors().get(0) instanceof Evaluator.AttributeWithValue);
    }

    @Test
    void parse_shouldReturnAndEvaluatorWithAttributeWithValueNot_whenQueryContainsAttributeWithValueNot() {
        Evaluator result = QueryParser.parse("[data-id!=value]");

        assertTrue(result instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) result).getEvalutors().get(0) instanceof Evaluator.AttributeWithValueNot);
    }

    @Test
    void parse_shouldReturnAndEvaluatorWithAttributeWithValueStarting_whenQueryContainsAttributeWithValueStarting() {
        Evaluator result = QueryParser.parse("[data-id^=value]");

        assertTrue(result instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) result).getEvalutors().get(0) instanceof Evaluator.AttributeWithValueStarting);
    }

    @Test
    void parse_shouldReturnAndEvaluatorWithAttributeWithValueEnding_whenQueryContainsAttributeWithValueEnding() {
        Evaluator result = QueryParser.parse("[data-id$=value]");

        assertTrue(result instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) result).getEvalutors().get(0) instanceof Evaluator.AttributeWithValueEnding);
    }

    @Test
    void parse_shouldReturnAndEvaluatorWithAttributeWithValueContaining_whenQueryContainsAttributeWithValueContaining() {
        Evaluator result = QueryParser.parse("[data-id*=value]");

        assertTrue(result instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) result).getEvalutors().get(0) instanceof Evaluator.AttributeWithValueContaining);
    }

    @Test
    void parse_shouldReturnAndEvaluatorWithAttributeWithValueMatching_whenQueryContainsAttributeWithValueMatching() {
        Evaluator result = QueryParser.parse("[data-id~=value]");

        assertTrue(result instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) result).getEvalutors().get(0) instanceof Evaluator.AttributeWithValueMatching);
    }

    @Test
    void parse_shouldReturnAllElementsEvaluator_whenQueryContainsAsterisk() {
        Evaluator result = QueryParser.parse("*");

        assertTrue(result instanceof Evaluator.AllElements);
    }

    @Test
    void parse_shouldReturnIndexLessThanEvaluator_whenQueryContainsIndexLessThan() {
        Evaluator result = QueryParser.parse(":lt(5)");

        assertTrue(result instanceof Evaluator.IndexLessThan);
    }

    @Test
    void parse_shouldReturnIndexGreaterThanEvaluator_whenQueryContainsIndexGreaterThan() {
        Evaluator result = QueryParser.parse(":gt(5)");

        assertTrue(result instanceof Evaluator.IndexGreaterThan);
    }

    @Test
    void parse_shouldReturnIndexEqualsEvaluator_whenQueryContainsIndexEquals() {
        Evaluator result = QueryParser.parse(":eq(5)");

        assertTrue(result instanceof Evaluator.IndexEquals);
    }

    @Test
    void parse_shouldReturnStructuralEvaluatorHas_whenQueryContainsHas() {
        Evaluator result = QueryParser.parse(":has(span)");

        assertTrue(result instanceof StructuralEvaluator.Has);
    }

    @Test
    void parse_shouldReturnEvaluatorContainsText_whenQueryContainsContains() {
        Evaluator result = QueryParser.parse(":contains(text)");

        assertTrue(result instanceof Evaluator.ContainsText);
    }

    @Test
    void parse_shouldReturnEvaluatorContainsOwnText_whenQueryContainsContainsOwn() {
        Evaluator result = QueryParser.parse(":containsOwn(text)");

        assertTrue(result instanceof Evaluator.ContainsOwnText);
    }

    @Test
    void parse_shouldReturnEvaluatorMatches_whenQueryContainsMatches() {
        Evaluator result = QueryParser.parse(":matches(regex)");

        assertTrue(result instanceof Evaluator.Matches);
    }

    @Test
    void parse_shouldReturnEvaluatorMatchesOwn_whenQueryContainsMatchesOwn() {
        Evaluator result = QueryParser.parse(":matchesOwn(regex)");

        assertTrue(result instanceof Evaluator.MatchesOwn);
    }

    @Test
    void parse_shouldReturnStructuralEvaluatorNot_whenQueryContainsNot() {
        Evaluator result = QueryParser.parse(":not(div)");

        assertTrue(result instanceof StructuralEvaluator.Not);
    }

    @Test
    void parse_shouldThrowSelectorParseException_whenQueryIsInvalid() {
        assertThrows(Selector.SelectorParseException.class, () -> QueryParser.parse("invalid query"));
    }
}