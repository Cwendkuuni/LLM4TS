package uk.me.jockmacmad.jstate.state;

import uk.me.jockmacmad.jstate.predicate.*;
import org.apache.commons.lang.builder.*;
import java.util.*;

public class Transition
{
    private final IState initialState;
    private final IState outcomeState;
    private final List<IGuardCondition> guardConditions;
    private final Event triggerEvent;
    private final String name;
    
    public Transition(final String pName, final Event pTriggerEvent, final IState pInitialState, final IState pOutcomeState) {
        this.guardConditions = new ArrayList<IGuardCondition>();
        this.name = pName;
        this.initialState = pInitialState;
        this.outcomeState = pOutcomeState;
        this.triggerEvent = pTriggerEvent;
    }
    
    public final void addGuardCondition(final IPredicate pGuard) {
        this.guardConditions.add((IGuardCondition)pGuard);
    }
    
    public final void addGuardCondition(final IGuardCondition pGuard) {
        this.guardConditions.add(pGuard);
    }
    
    @Override
    public final boolean equals(final Object pTransition) {
        return EqualsBuilder.reflectionEquals((Object)this, pTransition);
    }
    
    @Override
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode((Object)this);
    }
    
    protected void evaluateGuardConditions() {
    }
    
    public final IState getInitialState() {
        return this.initialState;
    }
    
    public final IState getOutcomeState() {
        return this.outcomeState;
    }
    
    public final Event getTriggerEvent() {
        return this.triggerEvent;
    }
    
    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString((Object)this);
    }
    
    public final IState trigger(final IState pCurrentState, final Event pEvent) {
        IState retVal = pCurrentState;
        boolean passesGuards = false;
        if (this.initialState.equals(pCurrentState) && this.triggerEvent.equals(pEvent)) {
            if (this.guardConditions.isEmpty()) {
                passesGuards = true;
            }
            else {
                for (final IGuardCondition ig : this.guardConditions) {
                    passesGuards = ig.evaluate(pEvent);
                }
            }
            if (passesGuards) {
                retVal = this.outcomeState;
            }
        }
        return retVal;
    }
    
    public final boolean willTrigger(final IState pCurrentState, final Event pEvent) {
        boolean retVal = false;
        boolean passesGuards = false;
        if (this.initialState.equals(pCurrentState) && this.triggerEvent.equals(pEvent)) {
            for (final IGuardCondition ig : this.guardConditions) {
                passesGuards = ig.evaluate(pEvent);
            }
            if (passesGuards) {
                retVal = true;
            }
        }
        return retVal;
    }
}
