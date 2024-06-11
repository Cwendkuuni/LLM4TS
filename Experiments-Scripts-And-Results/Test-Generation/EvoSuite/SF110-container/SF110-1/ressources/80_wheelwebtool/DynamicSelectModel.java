package wheel.util;

import wheel.components.*;
import wheel.*;
import java.util.*;

public class DynamicSelectModel implements ISelectModel
{
    private Component component;
    private StandaloneComponent topLevelComponent;
    private ElExpression label;
    private ElExpression value;
    private ElExpression collection;
    private ElExpression translator;
    private Collection objects;
    private Object emptyObject;
    
    public DynamicSelectModel(final ElExpression collection, final ElExpression label, final ElExpression value) {
        this.validate();
        this.label = label;
        this.value = value;
        this.collection = collection;
    }
    
    public DynamicSelectModel() {
    }
    
    public DynamicSelectModel collection(final String collection) {
        this.collection = new ElExpression(collection);
        return this;
    }
    
    public DynamicSelectModel label(final String label) {
        this.label = new ElExpression(label);
        return this;
    }
    
    public DynamicSelectModel value(final String value) {
        this.value = new ElExpression(value);
        return this;
    }
    
    public DynamicSelectModel enumeration(final String enumeration) {
        this.collection = new ElExpression(enumeration + ".values()");
        this.label = new ElExpression("toString()");
        this.value = new ElExpression("ordinal()");
        return this;
    }
    
    public DynamicSelectModel translator(final String translator) {
        this.translator = new ElExpression(translator);
        return this;
    }
    
    public DynamicSelectModel empty(final Object o) {
        this.emptyObject = o;
        return this;
    }
    
    @Override
    public int getOptionCount() {
        return this.getObjects().size();
    }
    
    @Override
    public String getLabel(final int index) {
        this.label.errorMessage("Could not evaluate expression " + this.label.getExpression() + " to create a label for dynamic ISelectModel. Index was " + index);
        final Object obj = this.label.eval(this.getObjects().toArray()[index], this.component);
        return obj.toString();
    }
    
    @Override
    public String getValue(final int index) {
        this.value.errorMessage("Could not evaluate expression " + (Object)this.value + " to create a value for dynamic ISelectModel. Index was " + index);
        final Object obj = this.value.eval(this.getObjects().toArray()[index], this.component);
        return obj.toString();
    }
    
    @Override
    public Object translateValue(final String value) {
        final Object[] array = this.getObjects().toArray();
        int i = 0;
        while (i < array.length) {
            final String compareTo = this.getValue(i);
            if (compareTo.equals(value)) {
                if (this.translator != null) {
                    this.translator.errorMessage("Could not evaluate expression " + (Object)this.translator + " to translate value ' " + value + "'");
                    return this.translator.eval(array[i], this.component);
                }
                return array[i];
            }
            else {
                ++i;
            }
        }
        return null;
    }
    
    @Override
    public Collection getObjects() {
        if (this.objects != null) {
            return this.objects;
        }
        this.collection.errorMessage("Could not evaluate expression " + (Object)this.collection + " to create a dynamic ISelectModel.");
        final Object obj = this.collection.eval(this.topLevelComponent, this.component);
        if (obj == null) {
            throw new WheelException("Expression " + (Object)this.collection + " evaluated to null. Can't build a dynamic ISelectModel", this.component);
        }
        if (obj instanceof Collection) {
            this.objects = (Collection)obj;
        }
        else {
            if (!(obj instanceof Object[])) {
                throw new WheelException("Expression " + (Object)this.collection + " evaluated to an object that is not an instance of Collection or Object[]. Can't build a dynamic ISelectModel", this.component);
            }
            final Object[] array = (Object[])obj;
            final Collection col = new ArrayList();
            for (int i = 0; i < array.length; ++i) {
                col.add(array[i]);
            }
            this.objects = col;
        }
        if (this.emptyObject != null) {
            final Collection newCollection = new LinkedList();
            newCollection.add(this.emptyObject);
            newCollection.addAll(this.objects);
            this.objects = newCollection;
        }
        return this.objects;
    }
    
    public Component getComponent() {
        return this.component;
    }
    
    public void setComponent(final Component component) {
        this.component = component;
    }
    
    public StandaloneComponent getTopLevelComponent() {
        return this.topLevelComponent;
    }
    
    public void setTopLevelComponent(final StandaloneComponent topLevelComponent) {
        this.topLevelComponent = topLevelComponent;
    }
    
    public void validate() {
        if (this.collection == null) {
            throw new WheelException("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", (Component)null);
        }
        if (this.label == null) {
            throw new WheelException("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", (Component)null);
        }
        if (this.value == null) {
            throw new WheelException("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", (Component)null);
        }
    }
    
    public void reset() {
        this.objects = null;
    }
}
