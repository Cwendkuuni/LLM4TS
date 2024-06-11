
package net.sourceforge.beanbin.reflect;

import net.sourceforge.beanbin.search.*;
import java.lang.annotation.*;
import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearch
{
    private Class clazz;
    private Query query;
    
    public ReflectionSearch(final Class clazz) {
        this.clazz = clazz;
        this.query = new Query();
    }
    
    public boolean hasAnnotation(final String term) {
        for (final Annotation anno : this.clazz.getAnnotations()) {
            final String name = anno.annotationType().getName();
            final String className = name.substring(name.lastIndexOf(".") + 1);
            final String termName = term.toString().substring(1);
            if (this.hasWildcard(termName)) {
                final WildcardSearch wild = new WildcardSearch(termName);
                if (wild.doesMatch(name)) {
                    return true;
                }
                if (wild.doesMatch(className)) {
                    return true;
                }
            }
            else {
                if (name.equals(termName)) {
                    return true;
                }
                if (className.equals(termName)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean hasWildcard(final String term) {
        return term.indexOf("*") != -1;
    }
    
    public ReflectionSearch methodsThatHave(final String term) throws BeanBinException {
        this.query.add(new MethodReflectionCriteria(this.clazz, term, SearchType.EQUALS));
        return this;
    }
    
    public ReflectionSearch methodsThatDontHave(final String term) throws BeanBinException {
        this.query.add(new MethodReflectionCriteria(this.clazz, term, SearchType.DOESNOTEQUAL));
        return this;
    }
    
    public ReflectionSearch and() {
        this.query.setNextConditional(Conditional.AND);
        return this;
    }
    
    public ReflectionSearch or() {
        this.query.setNextConditional(Conditional.OR);
        return this;
    }
    
    public List<Method> getMethods() throws BeanBinException {
        List<Method> list = (List<Method>)ReflectionShelf.getInstance().get(this.query);
        if (list == null) {
            list = new ArrayList<Method>();
            for (final Criteria criteria : this.query.getCriterias()) {
                if (criteria instanceof ReflectionCriteria) {
                    final ReflectionCriteria crit = (ReflectionCriteria)criteria;
                    final Conditional cond = crit.getPreviousCondition();
                    if (cond != null) {
                        if (cond == Conditional.AND) {
                            this.intersect(list, crit.getResults());
                        }
                        else {
                            this.union(list, crit.getResults());
                        }
                    }
                    else {
                        list = crit.getResults();
                    }
                }
            }
            ReflectionShelf.getInstance().put(this.query, list);
        }
        return list;
    }
    
    protected void intersect(final List<Method> master, final List<Method> toadd) {
        for (int i = 0; i < master.size(); ++i) {
            if (!toadd.contains(master.get(i))) {
                master.remove(i--);
            }
        }
    }
    
    protected void union(final List<Method> master, final List<Method> toadd) {
        for (final Method method : toadd) {
            if (!master.contains(method)) {
                master.add(method);
            }
        }
    }
}
