package glengineer.agents;

import glengineer.agents.settings.*;
import java.util.*;
import glengineer.*;
import javax.swing.*;
import glengineer.agents.setters.*;

public abstract class GroupAgent extends Agent
{
    protected LinkedList<Agent> childrenAgents;
    
    public GroupAgent() {
        this.childrenAgents = new LinkedList<Agent>();
        this.settings = new Settings();
    }
    
    @Override
    public String getFirstName() {
        if (this.childrenAgents.isEmpty()) {
            return null;
        }
        final Iterator<Agent> i = this.childrenAgents.iterator();
        while (i.hasNext()) {
            final String result = i.next().getFirstName();
            if (result != null) {
                return result;
            }
        }
        return null;
    }
    
    @Override
    public String getLastName() {
        if (this.childrenAgents.isEmpty()) {
            return null;
        }
        final Iterator<Agent> i = this.childrenAgents.descendingIterator();
        while (i.hasNext()) {
            final String result = i.next().getLastName();
            if (result != null) {
                return result;
            }
        }
        return null;
    }
    
    @Override
    public boolean isComponent(final String componentName) {
        return false;
    }
    
    @Override
    public boolean isGroup(final String firstName, final String lastName) {
        return this.getFirstName().equals(firstName) && this.getLastName().equals(lastName);
    }
    
    @Override
    public boolean isGap() {
        return false;
    }
    
    @Override
    public boolean equals(final Agent agent) {
        return agent.isGroup(this.getFirstName(), this.getLastName());
    }
    
    public void addAgent(final Agent agent) {
        if (agent == null) {
            throw new IllegalArgumentException("Cannot add null agents.");
        }
        this.childrenAgents.add(agent);
    }
    
    public List<Agent> getChildren() {
        return Collections.unmodifiableList((List<? extends Agent>)this.childrenAgents);
    }
    
    public int getNumberOfChildren() {
        return this.childrenAgents.size();
    }
    
    public ComponentAgent getComponent(final String name) {
        for (final Agent child : this.childrenAgents) {
            if (child.isComponent(name)) {
                return (ComponentAgent)child;
            }
        }
        throw new IllegalArgumentException("component " + name + " in the " + this + " not found");
    }
    
    public GroupAgent getGroup(final String firstName, final String lastName) {
        for (final Agent child : this.childrenAgents) {
            if (child.isGroup(firstName, lastName)) {
                return (GroupAgent)child;
            }
        }
        throw new IllegalArgumentException("the group <" + firstName + "," + lastName + "> in the " + this + " not found");
    }
    
    @Override
    public ComponentAgent findDependingComponentByName(final String componentName) {
        ComponentAgent result = null;
        for (final Agent child : this.childrenAgents) {
            result = child.findDependingComponentByName(componentName);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
    
    @Override
    public GroupAgent findDependingGroupByNames(final String firstName, final String lastName) {
        if (this.isGroup(firstName, lastName)) {
            return this;
        }
        GroupAgent result = null;
        for (final Agent child : this.childrenAgents) {
            result = child.findDependingGroupByNames(firstName, lastName);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
    
    @Override
    public ParallelGroupAgent findDependingParallelGroupByNames(final String firstName, final String lastName) {
        if (this.isGroup(firstName, lastName) && this instanceof ParallelGroupAgent) {
            return (ParallelGroupAgent)this;
        }
        GroupAgent result = null;
        for (final Agent child : this.childrenAgents) {
            result = child.findDependingParallelGroupByNames(firstName, lastName);
            if (result != null) {
                return (ParallelGroupAgent)result;
            }
        }
        return null;
    }
    
    @Override
    public SequentialGroupAgent findDependingSequentialGroupByNames(final String firstName, final String lastName) {
        if (this.isGroup(firstName, lastName) && this instanceof SequentialGroupAgent) {
            return (SequentialGroupAgent)this;
        }
        GroupAgent result = null;
        for (final Agent child : this.childrenAgents) {
            result = child.findDependingSequentialGroupByNames(firstName, lastName);
            if (result != null) {
                return (SequentialGroupAgent)result;
            }
        }
        return null;
    }
    
    public void removeGapsFromParallelGroups() {
        this.passTheCallToSubgroups();
        this.removeGapsFromParallelSubgroupsWithOtherContent();
        this.replaceParallelSubgroupsWithGapsByTheirSingleGaps();
        this.replaceContinuousSequencesOfGapsBySingleGaps();
        this.replaceGroupsWithSingleElementsByThatElements();
    }
    
    private void passTheCallToSubgroups() {
        for (final Agent child : this.childrenAgents) {
            if (child instanceof GroupAgent) {
                ((GroupAgent)child).removeGapsFromParallelGroups();
            }
        }
    }
    
    private void removeGapsFromParallelSubgroupsWithOtherContent() {
        for (final Agent child : this.childrenAgents) {
            if (child instanceof ParallelGroupAgent) {
                final ParallelGroupAgent parallel = (ParallelGroupAgent)child;
                if (parallel.containsGapsOnly()) {
                    continue;
                }
                parallel.removeAllGaps();
            }
        }
    }
    
    private void replaceParallelSubgroupsWithGapsByTheirSingleGaps() {
        final ListIterator<Agent> li = this.childrenAgents.listIterator();
        while (li.hasNext()) {
            final Agent child = li.next();
            if (child instanceof ParallelGroupAgent) {
                final ParallelGroupAgent parallel = (ParallelGroupAgent)child;
                final Agent gap = parallel.getGapsType();
                if (gap == null) {
                    continue;
                }
                li.set(gap);
            }
        }
    }
    
    private void replaceContinuousSequencesOfGapsBySingleGaps() {
        final ListIterator<Agent> li = this.childrenAgents.listIterator();
        while (li.hasNext()) {
            final Agent child = li.next();
            if (!child.isGap()) {
                continue;
            }
            final Agent currentGap = child;
            while (li.hasNext()) {
                final Agent nextGap = li.next();
                if (!nextGap.isGap()) {
                    break;
                }
                if (!nextGap.equals(currentGap)) {
                    throw new IllegalArgumentException("A sequence of gaps possessing different types was found in a group.");
                }
                li.remove();
            }
        }
    }
    
    private void replaceGroupsWithSingleElementsByThatElements() {
        final ListIterator<Agent> li = this.childrenAgents.listIterator();
        while (li.hasNext()) {
            final Agent child = li.next();
            if (child instanceof GroupAgent) {
                final GroupAgent nextSubgroup = (GroupAgent)child;
                if (nextSubgroup.getNumberOfChildren() != 1) {
                    continue;
                }
                li.set(nextSubgroup.getChildren().get(0));
            }
        }
    }
    
    public void replaceTemporaryGapsByRealGaps(final GroupLayoutEngineer.TemporaryGapsToGaps map) {
        final ListIterator<Agent> li = this.childrenAgents.listIterator();
        while (li.hasNext()) {
            final Agent child = li.next();
            if (child instanceof GroupAgent) {
                ((GroupAgent)child).replaceTemporaryGapsByRealGaps(map);
            }
            if (child instanceof TemporaryGapAgent) {
                li.set(map.gap((TemporaryGapAgent)child));
            }
        }
    }
    
    @Override
    public void addContentToGroup(final GroupLayout.Group parentGroup, final GroupCreatorAndElementAdder creatorAndAdder) {
        creatorAndAdder.addGroupToGroup(this.groupContent(creatorAndAdder), this.settings, parentGroup);
    }
    
    public abstract GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0);
    
    public abstract FunctionsOnGroup getFunctionsOnGroupImplemenation();
    
    public void addGapBefore(final GapAgent gap, final Agent element) {
        if (this.childrenAgents.isEmpty()) {
            this.childrenAgents.add(gap);
            return;
        }
        final ListIterator<Agent> iter = this.childrenAgents.listIterator();
        while (iter.hasNext()) {
            if (iter.next().equals(element)) {
                iter.previous();
                iter.add(gap);
                return;
            }
        }
        throw new IllegalArgumentException("the " + element.toString() + " in the " + this.toString() + " not found");
    }
    
    public void addGapAfter(final GapAgent gap, final Agent element) {
        if (this.childrenAgents.isEmpty()) {
            this.childrenAgents.add(gap);
            return;
        }
        final ListIterator<Agent> iter = this.childrenAgents.listIterator();
        while (iter.hasNext()) {
            if (iter.next().equals(element)) {
                iter.add(gap);
                return;
            }
        }
        throw new IllegalArgumentException("the " + element.toString() + " in the " + this.toString() + " not found");
    }
    
    protected static class FunctionsOnGroupAndElementImplementation implements FunctionsOnGroupAndElement
    {
        protected GroupAgent parentGroupAgent;
        protected Agent elementAgent;
        
        public FunctionsOnGroupAndElementImplementation(final GroupAgent parent, final Agent element) {
            this.parentGroupAgent = parent;
            this.elementAgent = element;
        }
        
        @Override
        public void addPrecedingGap(final int size) {
            this.parentGroupAgent.addGapBefore(new GapAgent(size), this.elementAgent);
        }
        
        @Override
        public void addPrecedingGap(final int min, final int pref, final int max) {
            this.parentGroupAgent.addGapBefore(new GapAgent(min, pref, max), this.elementAgent);
        }
        
        @Override
        public void addFollowingGap(final int size) {
            this.parentGroupAgent.addGapAfter(new GapAgent(size), this.elementAgent);
        }
        
        @Override
        public void addFollowingGap(final int min, final int pref, final int max) {
            this.parentGroupAgent.addGapAfter(new GapAgent(min, pref, max), this.elementAgent);
        }
    }
}
