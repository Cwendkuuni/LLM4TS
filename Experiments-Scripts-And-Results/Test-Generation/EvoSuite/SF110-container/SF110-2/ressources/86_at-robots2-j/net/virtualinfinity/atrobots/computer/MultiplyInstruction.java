package net.virtualinfinity.atrobots.computer;


/**
 * An {@link AbstractCombiningInstruction} which performs a multiplication on the operands.
 *
 * @author Daniel Pitts
 */
public class MultiplyInstruction extends AbstractCombiningInstruction {
    public MultiplyInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected int combine(short first, short second) {
        return first * second;
    }
}
