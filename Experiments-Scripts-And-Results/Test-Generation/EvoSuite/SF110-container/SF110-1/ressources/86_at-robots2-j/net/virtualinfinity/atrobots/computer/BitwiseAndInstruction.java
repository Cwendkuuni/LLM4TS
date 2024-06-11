package net.virtualinfinity.atrobots.computer;


/**
 * An {@link AbstractCombiningInstruction} which performs a bitwise and on the operands.
 *
 * @author Daniel Pitts
 */
public class BitwiseAndInstruction extends AbstractCombiningInstruction {
    public BitwiseAndInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected int combine(short first, short second) {
        return first & second;
    }
}
