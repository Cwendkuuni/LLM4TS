package src;

import java.util.*;

public class VassalRebellion extends Actions
{
    private Fiefdoms fiefdomObj;
    
    @Override
    public void perform(final Collection col) {
        final Iterator colIter = col.iterator();
        final String fiefdomName = colIter.next();
        this.fiefdomObj = Map.map.get(fiefdomName);
        final int personalCastles = this.fiefdomObj.getPersonalCastles();
        final int fiefdomCastles = this.fiefdomObj.getFiefdomCastles();
        if (Map.map.get(fiefdomName).getLoyalVassals().size() > 0) {
            if (this.fiefdomObj.getLoyalVassals().size() - this.fiefdomObj.getValue() >= 0) {
                this.fiefdomObj.setRebelliousVassals(this.fiefdomObj.getValue(), true);
                this.fiefdomObj.setKingdom(false);
            }
            else {
                this.fiefdomObj.setRebelliousVassals(this.fiefdomObj.getLoyalVassals().size(), true);
            }
            if ((personalCastles > 0 || fiefdomCastles > 0) && this.fiefdomObj.getLoyalVassals().size() - (personalCastles * 10 + fiefdomCastles * 25) > 0) {
                this.fiefdomObj.setRebelliousVassals(this.fiefdomObj.getLoyalVassals().size() - (personalCastles * 10 + fiefdomCastles * 25), true);
            }
        }
        else {
            new RollEvent(this.fiefdomObj.getName());
        }
    }
}
